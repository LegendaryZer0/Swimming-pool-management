package ru.softwave.pool.service;

import ru.softwave.pool.model.dto.GroupFindDto;
import ru.softwave.pool.model.entity.Group;

import java.util.List;

public interface GroupService {
  void saveGroup(Group group);

  Group updateGroup(Group group);

  Group createGroupSimple(String groupName);

  void addVisitorToGroup(String groupName, String visitorName);

  Group findGroupByName(String groupName);

  List<GroupFindDto> findAll();

  void putTrainerToGroup(String groupName, String trainerEmail);
}
