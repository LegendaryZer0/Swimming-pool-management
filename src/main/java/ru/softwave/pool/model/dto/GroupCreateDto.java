package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.ScriptAssert;
import ru.softwave.pool.model.entity.Group;
import ru.softwave.pool.util.validation.ValidStartEndTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ScriptAssert(
    lang = "javascript",
    script = "_this.maleMaxCount + _this.femaleMaxCount ===_this.groupCapacity",
    message = "GroupCapacity must be sum of male and female max counts")
@Slf4j
@ValidStartEndTime(
    message = "Group time must be a multiple of one hour",
    startTime = "startTime",
    endTime = "endTime")
public class GroupCreateDto {

  private Time startTime;

  private Time endTime;

  @NotEmpty(message = " name must be not empty")
  private String name;

  @PositiveOrZero private Byte groupCapacity;

  @PositiveOrZero private Byte maleMaxCount;
  @PositiveOrZero private Byte femaleMaxCount;

  public Group convertToGroup() {
    return Group.builder()
        .startTime(startTime)
        .endTime(endTime)
        .maleCurrentCount((byte) 0)
        .femaleCurrentCount((byte) 0)
        .femaleMaxCount(femaleMaxCount)
        .maleMaxCount(maleMaxCount)
        .name(name)
        .groupCapacity(groupCapacity)
        .build();
  }
}
