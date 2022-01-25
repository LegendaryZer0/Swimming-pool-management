package ru.softwave.pool.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    /**
     * Рекурсивно проходится по stackTrace собирая первые трейсы исключений.
     * Пример трейса:
     *
     * "java.lang.RuntimeException: Ошибка #1 -
     *      * ru.waveaccess.krst.projects.api.project.ProjectController.getProjects(ProjectController.java:53)"
     *
     * @param throwable исключение
     * @param stackTraceDeep начало погружения, должно быть 0
     * @return лист подготовленных трейсов
     */
    private List<String> walkStackTraceCause(final Throwable throwable,
                                             int stackTraceDeep) {
        List<String> stackTraces = new ArrayList<>();
        Optional.ofNullable(throwable.getStackTrace()).ifPresent((stackTrace -> {
            if (stackTrace.length > 0) {
                stackTraces.add(String.format("%s - %s",
                        throwable.toString(),
                        stackTrace[0].toString())
                );
            }
        }));
        Throwable cause = throwable.getCause();
        if (Objects.nonNull(cause)) {
            stackTraces.addAll(walkStackTraceCause(cause, stackTraceDeep + 1));
        }
        return stackTraces;
    }

    /**
     * Вызывает рекурсивную подготовку трейсов.
     * @param throwable исключение
     * @return лист подготовленных трейсов
     */
    private List<String> createStackTrace(final Throwable throwable) {
        return walkStackTraceCause(throwable, 0);
    }

    /**
     * Собирает ответ для ошибки
     * @param exceptionMessage сообщение ошибки
     * @param errorType тип ошибки
     * @param errors дополнительные сообщения при нескольких ошибках
     * @param stackTrace стектрейс ошибки
     * @param request запрос
     */
    private ResponseEntity<ErrorMessage> buildResponse(String exceptionMessage,
                                                                                             ErrorType errorType,
                                                                                             Map<String, String> errors,
                                                                                             List<String> stackTrace,
                                                                                             NativeWebRequest request) {
        String url = Optional.ofNullable(request)
                .map((r) -> r.getNativeRequest(HttpServletRequest.class))
                .map(HttpServletRequest::getRequestURI)
                .orElse("");

        var errorMessage = ErrorMessage.builder()
                .status(errorType.getStatus().value())
                .error(errorType.getTitle())
                .code(errorType.name())
                .message(exceptionMessage)
                .stacktrace(stackTrace)
                .errors(errors)
                .path(url);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name());
        return new ResponseEntity<>(errorMessage.build(), headers, errorType.getStatus());
    }

    /**
     * Собирает ответ для ошибки
     * @param exceptionMessage сообщение ошибки
     * @param errorType тип ошибки
     * @param errors дополнительные сообщения при нескольких ошибках
     * @param request запрос
     */
    private ResponseEntity<ErrorMessage> buildResponse(String exceptionMessage,
                                                                                             ErrorType errorType,
                                                                                             Map<String, String> errors,
                                                                                             NativeWebRequest request) {
        return buildResponse(exceptionMessage, errorType, errors, new ArrayList<>(), request);
    }

    /**
     * Собирает ответ для ошибки
     * @param exceptionMessage сообщение ошибки
     * @param errorType тип ошибки
     * @param request запрос
     */
    private ResponseEntity<ErrorMessage> buildResponse(String exceptionMessage,
                                                                                             ErrorType errorType,
                                                                                             NativeWebRequest request) {
        return buildResponse(exceptionMessage, errorType, new HashMap<>(), new ArrayList<>(), request);
    }

    /**
     * Перехватывает ошибку недействительных параметров метода
     * @param ex экземпляр ошибки класса MethodArgumentNotValidException
     * @param request запрос
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, NativeWebRequest request) {
        Map<String, String> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage));
        return buildResponse("Method argument not valid", ErrorType.VALIDATION_ERROR, errors, request);
    }

    /**
     * Перехватывает непредвиденную ошибку
     * @param ex экземпляр ошибки Exception
     * @param request запрос
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex, NativeWebRequest request) {
        log.error(ErrorType.UNEXPECTED_ERROR.getTitle(), ex);
        return buildResponse(ex.getMessage(),
                ErrorType.UNEXPECTED_ERROR,
                new HashMap<>(),
                createStackTrace(ex),
                request);
    }

    /**
     * Перехватывает AccessDeniedException
     * @param ex экземпляр ошибки Exception
     * @param request запрос
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException ex, NativeWebRequest request) {
        log.error(ErrorType.ACCESS_DENIED.getTitle(), ex);
        return buildResponse(ex.getMessage(),
                ErrorType.ACCESS_DENIED,
                new HashMap<>(),
                createStackTrace(ex),
                request);
    }

    /**
     * Перехватывает rest ошибку
     * @param ex экземпляр ошибки класса RestException
     * @param request запрос
     */
    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorMessage> handleRestException(RestException ex, NativeWebRequest request) {
        return buildResponse(ex.getMessage(), ex.getErrorType(), request);
    }
}
