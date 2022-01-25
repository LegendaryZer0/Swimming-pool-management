package ru.softwave.pool.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.softwave.pool.model.entity.RedisUser;
import ru.softwave.pool.repo.RedisUserRepository;
import ru.softwave.pool.security.authentication.JWTAuthentication;
import ru.softwave.pool.util.JwtUtil;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RedisUserServiceImpl implements RefreshTokenService, TokenAuthenticateService {
    @Autowired
    private RedisUserRepository redisUserRepository;
    @Autowired private ApiAuthService apiAuthService;
    @Autowired private JwtUtil jwtUtil;

    public Pair<String, String> getRefreshAccessTokensWithRotation(
            String oldRefreshToken, String accessToken, String deviceName) {

        String newRefreshToken =
                rotateToken(oldRefreshToken, deviceName)
                        .orElseThrow(() -> new BadCredentialsException("Malicious user taked token!"));

        String newAccessToken =
                apiAuthService.generateAccessToken(getExpiredTokenUsername(accessToken));
        authenticateUser(newAccessToken);
        return Pair.of(newAccessToken, newRefreshToken);
    }

    public void authenticateUser(String accessToken) {
        JWTAuthentication tokenAuthentication = new JWTAuthentication(accessToken);
        // tokenAuthentication.setAuthenticated(true);
        log.info(
                "extracted authority {}",
                jwtUtil
                        .extractClaim(
                                accessToken,
                                claims ->
                                        ((LinkedHashMap<String, String>) claims.get("role"))
                                                .values().stream().findFirst())
                        .toString());
        tokenAuthentication.setAuthority(
                jwtUtil.extractClaim(
                        accessToken,
                        claims ->
                                ((LinkedHashMap<String, String>) claims.get("role"))
                                        .values().stream().findFirst().get()));
        log.info("Token authentication is {}", tokenAuthentication);
        SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);
    }

    private String getExpiredTokenUsername(String expiredAccessToken) {
        try {
            return jwtUtil.extractUsername(expiredAccessToken);
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    public Optional<String> rotateToken(String oldRefreshToken, String deviceName) {
        log.info("old refresh token {}", oldRefreshToken);

        Optional<RedisUser> redisUser = redisUserRepository.findById(oldRefreshToken);
        log.info("redis user bt token {}", redisUser);
        if (redisUser.isPresent() && redisUser.get().getDeviceName().equals(deviceName)) {
            UUID newRefreshToken = UUID.randomUUID();
            redisUserRepository.delete(redisUser.get());
            redisUser.get().setRefreshToken(newRefreshToken.toString());
            redisUserRepository.save(redisUser.get());
            return Optional.of(newRefreshToken.toString());
        } else return Optional.empty();
    }

    public RedisUser addRedisUser(RedisUser redisUser) {
        return redisUserRepository.save(redisUser);
    }
}
