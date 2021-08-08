package ru.softwave.pool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.softwave.pool.model.dto.GroupCreateDto;
import ru.softwave.pool.model.dto.GroupFindDto;
import ru.softwave.pool.model.entity.Group;
import ru.softwave.pool.service.GroupService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/group-management")
public class GroupController {
  @Autowired private GroupService groupService;

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/groups")
  public ResponseEntity<String> saveGroup(@RequestBody @Valid GroupCreateDto group) {

    groupService.saveGroup(group.convertToGroup());
    return ResponseEntity.ok("OK");
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping(method = RequestMethod.PUT, value = "/groups")
  public ResponseEntity<?> updateGroup(Group group) {

    return ResponseEntity.ok(groupService.updateGroup(group));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/groups/{group-name}")
  public ResponseEntity<?> createGroupSimple(@PathVariable("group-name") String groupName) {
    return ResponseEntity.ok(groupService.createGroupSimple(groupName));
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/groups/{group-name}")
  public Group findGroupByName(@PathVariable("group-name") String groupName) {

    return groupService.findGroupByName(groupName);
  }

  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('VISITOR') or hasAuthority('TRAINER')")
  @GetMapping("/groups")
  public List<GroupFindDto> findAllGroups() {
    return groupService.findAll();
  }

  @PreAuthorize("hasAuthority('VISITOR')")
  @RequestMapping(method = RequestMethod.POST, value = "/groups/{group-name}/visitor")
  public String addVisitorToGroup(
      @PathVariable("group-name") String groupName, Principal principal) {
    groupService.addVisitorToGroup(groupName, principal.getName());
    return "OK";
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping(
      method = RequestMethod.PUT,
      value = "/groups/{group-name}/trainer/{trainer-email}")
  public String putTrainerToGroup(
      @PathVariable("group-name") String groupName,
      @PathVariable("trainer-email") String trainerEmail) {
    groupService.putTrainerToGroup(groupName, trainerEmail);
    return "OK";
  }
}
