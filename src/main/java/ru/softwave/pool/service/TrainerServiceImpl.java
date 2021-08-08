package ru.softwave.pool.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.softwave.pool.model.dto.AvailableTrainersDto;
import ru.softwave.pool.model.entity.Trainer;
import ru.softwave.pool.repo.TrainerRepository;
import ru.softwave.pool.util.comparator.AvailableTrainersPreferredTimeComparator;

import javax.persistence.EntityNotFoundException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {
  @Autowired private TrainerRepository trainerRepository;
  @Autowired private AvailableTrainersPreferredTimeComparator trainerComparator;
  @Autowired private PasswordEncoder passwordEncoder;

  public void registrTrainer(Trainer trainer) {
    trainer.setHashPassword(passwordEncoder.encode(trainer.getHashPassword()));
    trainerRepository.save(trainer);
  }

  public void saveTrainer(Trainer trainer) {
    trainerRepository.save(trainer);
  }

  public List<AvailableTrainersDto> getAvailableTrainers(String[] sortProps) {
    Sort sort;
    if (sortProps[0].equals("None")) {
      sort = Sort.unsorted();
    } else {
      sort = Sort.by(sortProps);
    }
    return AvailableTrainersDto.from(
        trainerRepository.findAllWhereCurrentWorkHoursLessThanMaxWorkHours(sort));
  }

  @Override
  public List<AvailableTrainersDto> getAvailableTrainersSortedByTimeIntersection(
      String groupStartTimeStr, String groupEndTimeStr) {
    Time groupStartTime = Time.valueOf(groupStartTimeStr);
    Time groupEndTime = Time.valueOf(groupEndTimeStr);
    List<AvailableTrainersDto> availableTrainers =
        AvailableTrainersDto.from(
            trainerRepository.findAllWhereCurrentWorkHoursLessThanMaxWorkHours(Sort.unsorted()));
    trainerComparator.setGroupTimeEndSec(convertTimeToSeconds(groupEndTime));
    trainerComparator.setGroupTimeStartSec(convertTimeToSeconds(groupStartTime));
    log.info("starting sorting");
    availableTrainers.sort(trainerComparator);
    log.info("available trainers list {}", availableTrainers);
    return availableTrainers;
  }

  public Trainer findTrainerByEmail(String email) {

    return trainerRepository
        .findByEmail(email)
        .orElseThrow(
            () ->
                new EntityNotFoundException("There is no trainer with this email address" + email));
  }

  private long convertTimeToSeconds(Time time) {
    LocalTime localTime = time.toLocalTime();
    return localTime.getHour() * 3600 + localTime.getMinute() * 60 + localTime.getSecond();
  }
}
