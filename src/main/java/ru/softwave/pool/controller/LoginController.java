package ru.softwave.pool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.softwave.pool.model.dto.UserLoginDto;
import ru.softwave.pool.service.ApiAuthService;
import ru.softwave.pool.util.JwtUtil;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class LoginController {

    @Autowired private ApiAuthService apiAuthService;

    @PostMapping("/authenticate")
    @PermitAll
    public String resolveAccess(
            @RequestBody UserLoginDto userLoginDto, Device device, HttpServletResponse response) {
        try {

            log.info("user tо authenticate {}", userLoginDto);
            return apiAuthService.processUser(userLoginDto, response, device.getDevicePlatform().name());
        } catch (BadCredentialsException e) {
            throw new IllegalStateException(e);
        }
    }
}
