package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.softwave.pool.model.entity.PreferedTimeEntity;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferedTimeDto {
  private Time startTime;
  private Time endTime;

  public static PreferedTimeDto from(PreferedTimeEntity preferedTimeEntity) {
    return PreferedTimeDto.builder()
        .startTime(preferedTimeEntity.getStartTime())
        .endTime(preferedTimeEntity.getEndTime())
        .build();
  }

  public PreferedTimeEntity convertToPreferedTimeEntity() {
    return PreferedTimeEntity.builder().startTime(startTime).endTime(endTime).build();
  }
}
