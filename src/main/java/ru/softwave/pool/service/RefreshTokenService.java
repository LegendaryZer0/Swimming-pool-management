package ru.softwave.pool.service;

import org.springframework.data.util.Pair;
import ru.softwave.pool.model.entity.RedisUser;

public interface RefreshTokenService {
    Pair<String, String> getRefreshAccessTokensWithRotation(
            String refreshToken, String accessToken, String deviceName);

    RedisUser addRedisUser(RedisUser redisUser);
}
