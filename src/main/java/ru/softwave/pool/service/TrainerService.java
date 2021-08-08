package ru.softwave.pool.service;

import ru.softwave.pool.model.dto.AvailableTrainersDto;
import ru.softwave.pool.model.entity.Trainer;

import java.util.List;

public interface TrainerService {
  void registrTrainer(Trainer trainer);

  void saveTrainer(Trainer trainer);

  List<AvailableTrainersDto> getAvailableTrainers(String[] sortProps);

  List<AvailableTrainersDto> getAvailableTrainersSortedByTimeIntersection(
      String groupStartTime, String groupEndTime);

  Trainer findTrainerByEmail(String email);
}
