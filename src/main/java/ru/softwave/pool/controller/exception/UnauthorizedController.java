package ru.softwave.pool.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softwave.pool.model.dto.ExceptionDto;

import java.time.LocalDateTime;

@RestController
public class UnauthorizedController {

  @PostMapping("/unauthorized")
  public ResponseEntity<?> getErrorMessage() {
    return ResponseEntity.ok(
        ExceptionDto.builder()
            .localDateTime(LocalDateTime.now())
            .message("email or password are wrong")
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .build());
  }
}
