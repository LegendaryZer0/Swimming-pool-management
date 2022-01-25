package ru.softwave.pool.service;

import ru.softwave.pool.model.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

  void sendConfirmEmail(User user);

  void confirmEmail(UUID uuid);

  public User findOrThrow(Long userId);

  public Optional<User> getUser(String login);
}
