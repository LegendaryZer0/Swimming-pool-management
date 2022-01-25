package ru.softwave.pool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.softwave.pool.exception.ErrorType;
import ru.softwave.pool.exception.Exc;
import ru.softwave.pool.model.dto.TokenDto;
import ru.softwave.pool.model.dto.UserLoginDto;
import ru.softwave.pool.model.entity.RedisUser;
import ru.softwave.pool.model.entity.User;
import ru.softwave.pool.repo.RedisUserRepository;
import ru.softwave.pool.util.JwtUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ApiAuthServiceImpl implements ApiAuthService {
    @Autowired
    public RedisUserRepository redisUserRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired private RefreshTokenService refreshTokenService;
    @Autowired private UserService userService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    public String processUser(
            UserLoginDto userLoginDto, HttpServletResponse response, String deviceName) {
        log.info("userLoginDto {}",userLoginDto);
        Optional<User> userOptional =
                userService.getUser(userLoginDto.getLogin());
    if (userOptional.isPresent() && passwordEncoder.matches(userLoginDto.getPassword(),userOptional.get().getHashPassword())) {
            UUID refreshToken = UUID.randomUUID();
            String accessToken = generateAccessToken(userLoginDto.getLogin());
            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken.toString());
            Cookie accessTokenCookie = new Cookie("access_token", accessToken);
            addHttpOnlyCookiesToUser(response, refreshTokenCookie, accessTokenCookie);
            addUserInRedis(userOptional.get(), refreshToken.toString(), deviceName);
            log.info(" refresh token is :: {}", refreshToken);

        } else {
        response.setStatus(403);
        throw Exc.gen(ErrorType.ENTITY_NOT_FOUND,"Сущность не найдена, неправильный логин или пароль");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userLoginDto.getLogin());
    return jwtUtil.generateToken(userDetails);
    }

    private void addHttpOnlyCookiesToUser(HttpServletResponse response, Cookie... cookies) {
        Arrays.stream(cookies).peek(x -> x.setHttpOnly(true)).forEach(response::addCookie);
    }

    public void addUserInRedis(User user, String refreshToken, String deviceName) {
        log.info("devicename is {} and user id is {}", deviceName, user.getId());

        Optional<RedisUser> redisUser =
                redisUserRepository.findRedisUserByDeviceNameAndUserId(deviceName, user.getId());
        log.info("founded redis user {}", redisUser);
        redisUser.ifPresent(value -> redisUserRepository.delete(value));

        redisUserRepository.save(
                RedisUser.builder()
                        .userId(user.getId())
                        .refreshToken(refreshToken)
                        .deviceName(deviceName)
                        .build());
    }

    public String generateAccessToken(String email) {
        log.info("email {}", email);
        return jwtUtil.generateToken(userDetailsService.loadUserByUsername(email));
    }
}
