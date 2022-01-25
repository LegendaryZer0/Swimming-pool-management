package ru.softwave.pool.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class ErrorMessage {
    private String error;
    private Integer status;
    private String message;
    private String code;
    private String path;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> stacktrace;
}
