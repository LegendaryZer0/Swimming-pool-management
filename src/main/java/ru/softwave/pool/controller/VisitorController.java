package ru.softwave.pool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.softwave.pool.model.dto.VisitorDto;
import ru.softwave.pool.service.VisitorService;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/visitor-management")
public class VisitorController {
  @Autowired private VisitorService visitorService;

  @PermitAll
  @PostMapping("/visitor")
  public ResponseEntity<?> registrVisitor(@Valid @RequestBody VisitorDto visitorDto) {
    log.info("Started to work Visitor dto is {}", visitorDto);
    visitorService.registrVisitor(visitorDto.convertToVisitor());
    return ResponseEntity.ok("OK");
  }
}
