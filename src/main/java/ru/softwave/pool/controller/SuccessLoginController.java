package ru.softwave.pool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softwave.pool.service.UserService;

import javax.annotation.security.PermitAll;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/success")
public class SuccessLoginController {

  @Autowired private UserService userService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/welcome")
  public String successLogin() {

    return "Successfully logged";
  }

  @PermitAll
  @GetMapping("/confirm/{uuid}")
  public String confirmAccount(@PathVariable("uuid") UUID id) {
    userService.confirmEmail(id);
    return "Confirmed";
  }
}
