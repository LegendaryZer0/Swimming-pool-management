package ru.softwave.pool.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.softwave.pool.model.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {}
