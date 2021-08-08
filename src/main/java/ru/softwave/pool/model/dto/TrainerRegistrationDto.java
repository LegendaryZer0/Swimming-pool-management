package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ScriptAssert;
import ru.softwave.pool.model.entity.Trainer;
import ru.softwave.pool.model.entity.User;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ScriptAssert(
    lang = "javascript",
    script = "(_this.maxHoursWork >=_this.minHoursWork)",
    message = "max hours works must be greater or than min hours work or equal")
public class TrainerRegistrationDto {
  @Email(message = "wrong email")
  @NotNull(message = "email must be not null")
  private String username;

  @NotNull(message = "password must not be null")
  @NotEmpty(message = "password must not be empty")
  private String password;

  @NotNull(message = "Name must be")
  @NotEmpty(message = " name must not be empty")
  private String name;

  @PositiveOrZero(message = "must be positive or zero")
  @Max(value = 127, message = "Max hours can not  be greater than 127")
  private int maxHoursWork;

  @Max(value = 127, message = "Max hours can not  be greater than 127")
  @PositiveOrZero(message = "must be positive or zero")
  private int minHoursWork;

  private PreferedTimeDto preferedTimeDto;

  public Trainer convertToTrainer() {
    return Trainer.builder()
        .email(username)
        .hashPassword(password)
        .name(name)
        .preferedTimeEntity(preferedTimeDto.convertToPreferedTimeEntity())
        .minWorkWeekHours((byte) minHoursWork)
        .maxWorkWeekHours((byte) maxHoursWork)
        .role(User.Role.TRAINER)
        .currentWorkWeekHours((byte) 0)
        .build();
  }
}
