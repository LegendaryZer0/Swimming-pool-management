package ru.softwave.pool.repo;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.softwave.pool.model.entity.Trainer;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

  @Query("from Trainer t where t.currentWorkWeekHours < t.maxWorkWeekHours")
  List<Trainer> findAllWhereCurrentWorkHoursLessThanMaxWorkHours(Sort sort);

  @Query("select t from Trainer t where t.email = ?1")
  Optional<Trainer> findByEmail(String email);
}
