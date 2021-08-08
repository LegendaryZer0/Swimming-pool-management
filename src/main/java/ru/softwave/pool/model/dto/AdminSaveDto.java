package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.softwave.pool.model.entity.Admin;
import ru.softwave.pool.model.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminSaveDto {
  @Email(message = "wrong email")
  private String username;

  @NotNull(message = "password must not be null")
  @NotEmpty(message = "password must not be empty")
  private String password;

  @NotEmpty(message = "Name must not be empty")
  @NotNull(message = "Name must  be")
  private String name;

  private String secondName;

  public Admin convertToAdmin() {
    return Admin.builder()
        .email(username)
        .hashPassword(password)
        .role(User.Role.ADMIN)
        .name(name)
        .surname(secondName)
        .build();
  }
}
