package ru.softwave.pool.exception;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {
    private final ErrorType errorType;
    private final String message;

    public RestException(ErrorType errorType) {
        super();
        this.errorType = errorType;
        this.message = "";
    }

    public RestException(ErrorType errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
        this.message = "";
    }

    public RestException(ErrorType errorType, String message) {
        super();
        this.errorType = errorType;
        this.message = message;
    }

    public RestException(ErrorType errorType, String message, Throwable cause) {
        super(cause);
        this.errorType = errorType;
        this.message = message;
    }
}
