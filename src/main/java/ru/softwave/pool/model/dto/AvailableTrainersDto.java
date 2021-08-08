package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ScriptAssert;
import ru.softwave.pool.model.entity.Trainer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ScriptAssert(
    lang = "javascript",
    script =
        "(_this.maxHoursWork >=_this.minHoursWork) &&(_this.currentWorkWeekHours<=_this.maxHoursWork)",
    message = "max hours works must be greater or than min hours work or equal")
public class AvailableTrainersDto {
  @PositiveOrZero(message = "must be positive or zero")
  private Byte minWorkWeekHours;

  @PositiveOrZero(message = "must be positive or zero")
  private Byte currentWorkWeekHours;

  @PositiveOrZero(message = "must be positive or zero")
  private Byte maxWorkWeekHours;

  @NotEmpty(message = "Name must be, name must not be empty")
  private String name;

  private String surname;

  @Email(message = "wrong email")
  private String email;

  private PreferedTimeDto preferedTimeDto;
  private List<TakenGroupDto> takenGroupDtoList;

  public static AvailableTrainersDto from(Trainer trainer) {
    return AvailableTrainersDto.builder()
        .email(trainer.getEmail())
        .currentWorkWeekHours(trainer.getCurrentWorkWeekHours())
        .maxWorkWeekHours(trainer.getMaxWorkWeekHours())
        .minWorkWeekHours(trainer.getMinWorkWeekHours())
        .name(trainer.getName())
        .surname(trainer.getSurname())
        .takenGroupDtoList(
            trainer.getGroups().stream().map(TakenGroupDto::from).collect(Collectors.toList()))
        .preferedTimeDto(PreferedTimeDto.from(trainer.getPreferedTimeEntity()))
        .build();
  }

  public static List<AvailableTrainersDto> from(List<Trainer> trainers) {
    return trainers.stream().map(AvailableTrainersDto::from).collect(Collectors.toList());
  }
}
