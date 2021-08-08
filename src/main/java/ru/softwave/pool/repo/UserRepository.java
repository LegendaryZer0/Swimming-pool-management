package ru.softwave.pool.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softwave.pool.model.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> getUserByEmail(String email);

  Optional<User> findByTechnicalInfo_UuidToConfirmEquals(UUID uuidToConfirm);
}
