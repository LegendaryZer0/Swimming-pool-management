package ru.softwave.pool.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softwave.pool.model.dto.GroupFindDto;
import ru.softwave.pool.model.entity.Group;
import ru.softwave.pool.model.entity.Trainer;
import ru.softwave.pool.model.entity.Visitor;
import ru.softwave.pool.repo.GroupRepository;

import javax.persistence.EntityNotFoundException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

  private static final byte WORKING_DAYS = 7;
  @Autowired private GroupRepository groupRepository;
  @Autowired private VisitorService visitorService;
  @Autowired private TrainerService trainerService;

  public void saveGroup(Group group) {
    groupRepository.save(group);
  }

  public Group createGroupSimple(String groupName) {
    Group generatedGroup =
        Group.builder()
            .groupCapacity((byte) 0)
            .femaleMaxCount((byte) 0)
            .maleMaxCount((byte) 0)
            .femaleCurrentCount((byte) 0)
            .maleCurrentCount((byte) 0)
            .endTime(Time.valueOf(LocalTime.of(0, 0)))
            .startTime(Time.valueOf(LocalTime.of(0, 0)))
            .name(groupName)
            .build();
    return groupRepository.save(generatedGroup);
  }

  public Group updateGroup(Group group) {
    return groupRepository.save(group);
  }

  public List<GroupFindDto> findAll() {
    return GroupFindDto.from(groupRepository.findAll());
  }

  public Group findGroupByName(String groupName) {
    log.info("group name {}", groupName);
    return groupRepository
        .findByName(groupName)
        .orElseThrow(() -> new EntityNotFoundException("There is no such group"));
  }

  @Transactional
  public void addVisitorToGroup(String groupName, String email) {

    Visitor visitor = visitorService.findVisitorByEmail(email);
    Group group =
        groupRepository
            .findByName(groupName)
            .orElseThrow(() -> new EntityNotFoundException("There is no such group"));
    if (group.getVisitors().contains(visitor)) {
      throw new IllegalStateException("that user is already in this group");
    }
    resolveGroupFreeSpace(group, visitor);

    visitor.getGroups().add(group);
    log.info("group is {}", group);
    log.info("visitor is {}", visitor);
    visitorService.saveVisitor(visitor);
  }

  private void resolveGroupFreeSpace(Group group, Visitor visitor) {
    if (visitor.getSex()) {
      byte maleCurrentCount = group.getMaleCurrentCount();
      byte maleMaxCount = group.getMaleMaxCount();
      if (maleCurrentCount < maleMaxCount) {
        group.setMaleCurrentCount((byte) (maleCurrentCount + 1));

      } else {
        throw new IllegalStateException("There is no space for male");
      }
    } else {
      byte femaleCurrentCount = group.getFemaleCurrentCount();
      byte femaleMaxCount = group.getFemaleMaxCount();
      if (femaleCurrentCount < femaleMaxCount) {
        group.setFemaleCurrentCount((byte) (femaleCurrentCount + 1));
      } else {
        throw new IllegalStateException("There is no space for female");
      }
    }
  }

  @Transactional
  public void putTrainerToGroup(String groupName, String trainerEmail) {
    Group group = findGroupByName(groupName);
    Trainer trainer = trainerService.findTrainerByEmail(trainerEmail);

    checkGroupOnTimeMultiples(group);
    checkTrainerOnFreeTime(trainer, group);

    Trainer oldTrainer = group.getTrainer();
    if (oldTrainer != null) {
      if (checkTrainerOnMinTimeEnough(oldTrainer, getGroupTimeDurationInHrs(group))) {

        addWorkHoursToTrainer(trainer, group);
        group.setTrainer(trainer);

        groupRepository.save(group);
        releaseTrainerTime(oldTrainer, group);
        trainerService.saveTrainer(oldTrainer);

      } else {
        throw new IllegalArgumentException(
            "Unable to replace a trainer in a group: The current trainer will not have a minimum number of hours");
      }
    }
    addWorkHoursToTrainer(trainer, group);
    group.setTrainer(trainer);
    log.debug("saved group {}", group);
    groupRepository.save(group);
  }

  private void addWorkHoursToTrainer(Trainer trainer, Group group) {
    trainer.setCurrentWorkWeekHours(
        (byte)
            (trainer.getCurrentWorkWeekHours() + getGroupTimeDurationInHrs(group) * WORKING_DAYS));
  }

  private void checkGroupOnTimeMultiples(Group group) {
    int groupStartMinuts = group.getStartTime().toLocalTime().getMinute();
    int groupEndMinuts = group.getEndTime().toLocalTime().getMinute();
    if (Math.abs(
            Math.max(groupStartMinuts, groupEndMinuts) - Math.min(groupStartMinuts, groupEndMinuts))
        != 0) {
      throw new IllegalStateException("Time of group need to be multiples 1 hour or so");
    }
  }

  private void checkTrainerOnFreeTime(Trainer trainer, Group group) {
    long trainerFreeTimeHrs = trainer.getMaxWorkWeekHours() - trainer.getCurrentWorkWeekHours();
    if (trainerFreeTimeHrs < getGroupTimeDurationInHrs(group)) {
      throw new IllegalArgumentException("This trainer does not have enough time for this group");
    }
  }

  private boolean checkTrainerOnMinTimeEnough(Trainer trainer, long hrsToRelease) {
    return trainer.getCurrentWorkWeekHours() - hrsToRelease >= trainer.getMinWorkWeekHours();
  }

  private void releaseTrainerTime(Trainer trainer, Group group) {
    trainer.setCurrentWorkWeekHours(
        (byte)
            (trainer.getCurrentWorkWeekHours() - getGroupTimeDurationInHrs(group) * WORKING_DAYS));
  }

  private long getGroupTimeDurationInHrs(Group group) {
    long timeInHrs =
        Duration.between(group.getStartTime().toLocalTime(), group.getEndTime().toLocalTime())
            .toHours();
    if (timeInHrs < 0) {
      timeInHrs += 24;
    }
    if (group.getStartTime().toLocalTime().getSecond()
        > group.getEndTime().toLocalTime().getSecond()) {
      timeInHrs += 1;
    }
    return timeInHrs;
  }
}
