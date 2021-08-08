package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.softwave.pool.model.entity.Group;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupFindDto {
  private String name;
  private Time startTime;
  private Time endTime;
  private Byte maleCurrentCount;
  private Byte femaleCurrentCount;
  private Byte maleMaxCount;
  private byte femaleMaxCount;
  private String trainerName;

  public static GroupFindDto from(Group group) {
    GroupFindDto groupFindDto =
        GroupFindDto.builder()
            .name(group.getName())
            .startTime(group.getStartTime())
            .endTime(group.getEndTime())
            .maleCurrentCount(group.getMaleCurrentCount())
            .femaleCurrentCount(group.getFemaleCurrentCount())
            .maleMaxCount(group.getMaleMaxCount())
            .femaleMaxCount(group.getFemaleMaxCount())
            .build();
    if (group.getTrainer() != null) {
      groupFindDto.setTrainerName(group.getTrainer().getName());
    }
    return groupFindDto;
  }

  public static List<GroupFindDto> from(List<Group> groups) {
    return groups.stream().map(GroupFindDto::from).collect(Collectors.toList());
  }
}
