package ru.softwave.pool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softwave.pool.model.dto.AvailableTrainersDto;
import ru.softwave.pool.model.dto.TrainerRegistrationDto;
import ru.softwave.pool.service.TrainerService;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/trainer-management")
public class TrainerController {
  @Autowired private TrainerService trainerService;

  @PermitAll
  @PostMapping("/trainers")
  public String saveTrainer(@Valid @RequestBody TrainerRegistrationDto trainerRegistrationDto) {
    trainerService.registrTrainer(trainerRegistrationDto.convertToTrainer());
    return "OK";
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/trainers")
  public List<AvailableTrainersDto> getAvailableTrainers(@RequestParam String[] sortProperties) {
    log.info("sortProperties {}", Arrays.toString(sortProperties));
    return trainerService.getAvailableTrainers(sortProperties);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/trainers/sorted/intersection")
  public List<AvailableTrainersDto> getAvailableTrainersSortedByIntersection(
      @RequestParam("timeStart") String groupTimeStart,
      @RequestParam("timeEnd") String groupTimeEnd) {
    log.info("time is {}   and {}", groupTimeStart, groupTimeEnd);
    return trainerService.getAvailableTrainersSortedByTimeIntersection(
        groupTimeStart, groupTimeEnd);
  }
}
