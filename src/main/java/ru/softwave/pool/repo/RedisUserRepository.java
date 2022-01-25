package ru.softwave.pool.repo;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;
import ru.softwave.pool.model.entity.RedisUser;

import java.util.Optional;
@Repository
public interface RedisUserRepository extends KeyValueRepository<RedisUser, String> {
  Optional<RedisUser> findRedisUserByUserId(Long userId);

  Optional<RedisUser> findRedisUserByDeviceNameAndUserId(String deviceName, Long userId);

  void deleteRedisUserByDeviceNameAndUserId(String deviceName, Long userId);
}
