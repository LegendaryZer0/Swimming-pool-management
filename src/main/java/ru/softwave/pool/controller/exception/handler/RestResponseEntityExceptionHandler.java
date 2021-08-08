package ru.softwave.pool.controller.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.softwave.pool.model.dto.ExceptionDto;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(
      value = {
        IllegalArgumentException.class,
        IllegalStateException.class,
        EntityNotFoundException.class,
        EntityNotFoundException.class,
        DataIntegrityViolationException.class
      })
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    ex.printStackTrace();
    String bodyOfResponse = ex.getMessage();
    return handleExceptionInternal(
        ex,
        ExceptionDto.builder()
            .message(bodyOfResponse)
            .statusCode(HttpStatus.CONFLICT.value())
            .localDateTime(LocalDateTime.now())
            .build(),
        new HttpHeaders(),
        HttpStatus.CONFLICT,
        request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ex.printStackTrace();
    List<String> validationList =
        ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
    log.warn("Validation error list : {} ", validationList);
    return new ResponseEntity<>(
        ExceptionDto.builder()
            .message(validationList.toString())
            .localDateTime(LocalDateTime.now())
            .statusCode(409)
            .build(),
        status);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ex.printStackTrace();
    ExceptionDto exceptionDto =
        ExceptionDto.builder()
            .localDateTime(LocalDateTime.now())
            .statusCode(status.value())
            .message(ex.getLocalizedMessage())
            .build();
    return handleExceptionInternal(ex,exceptionDto,headers,status,request);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public final ResponseEntity<ExceptionDto> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    ex.printStackTrace();
    ExceptionDto exceptionDto = ExceptionDto.builder().message(ex.getLocalizedMessage()).localDateTime(LocalDateTime.now()).statusCode(HttpStatus.FORBIDDEN.value()).build();
    return new ResponseEntity<>(exceptionDto, HttpStatus.FORBIDDEN);
  }



}
