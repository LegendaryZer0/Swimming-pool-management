package ru.softwave.pool.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorType {
    UNEXPECTED_ERROR(HttpStatus.BAD_REQUEST, "Неожиданная ошибка"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Ошибка валидации"),

    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "Не найдено"),
    ENTITY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Уже существует"),
    INCORRECT_PARAMETERS(HttpStatus.BAD_REQUEST, "Некорректные параметры"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Доступ запрещен"),
    HAS_DEPENDENT(HttpStatus.BAD_REQUEST, "Связанная сущность"),
    PDF_CREATION_FAILED(HttpStatus.EXPECTATION_FAILED, "Ошибка создания PDF"),
    XSL_CREATION_FAILED(HttpStatus.EXPECTATION_FAILED, "Ошибка создания XSL"),
    MEDIA_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "Ошибка загрузки"),
    MEDIA_DOWNLOAD_ERROR(HttpStatus.BAD_REQUEST, "Ошибка выгрузки"),

    PROJECT_MODIFICATION_DENIED(HttpStatus.METHOD_NOT_ALLOWED, "Проект в данном статусе не может быть изменён"),
    PROJECT_STATUS_CHANGE_DENIED(HttpStatus.METHOD_NOT_ALLOWED,
            "Ошибка изменения статуса проекта"),
    PROJECT_DELETION_DENIED(HttpStatus.METHOD_NOT_ALLOWED, "Проект в данном статусе не может быть удален"),
    COMMENT_MODIFICATION_DENIED(HttpStatus.METHOD_NOT_ALLOWED, "Комментарии в данном статусе проекта не могут быть изменены");


    private final HttpStatus status;
    private final String title;
}
