package ru.softwave.pool.service;

import ru.softwave.pool.model.dto.TokenDto;
import ru.softwave.pool.model.dto.UserLoginDto;

import javax.servlet.http.HttpServletResponse;

public interface ApiAuthService {
    String generateAccessToken(String email);

    String processUser(UserLoginDto userLoginDto, HttpServletResponse response, String deviceName);
}
