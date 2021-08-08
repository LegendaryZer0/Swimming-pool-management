package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.softwave.pool.model.entity.Group;

import java.sql.Time;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TakenGroupDto {
  private Time timeStart;
  private Time timeEnd;
  private String groupName;

  public static TakenGroupDto from(Group group) {
    return TakenGroupDto.builder()
        .groupName(group.getName())
        .timeEnd(group.getEndTime())
        .timeStart(group.getStartTime())
        .build();
  }
}
