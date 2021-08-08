package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.softwave.pool.model.entity.User;
import ru.softwave.pool.model.entity.Visitor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitorDto {
  @Email(message = "wrong email")
  @NotNull(message = "email must be not null")
  private String username;

  @NotNull(message = "password must not be null")
  @NotEmpty(message = "password must not be empty")
  private String password;

  @NotNull(message = "sex mus be not null")
  private boolean sex;

  @NotNull(message = "name must be  ")
  @NotEmpty(message = "name must not be empty")
  private String name;

  public Visitor convertToVisitor() {
    return Visitor.builder()
        .name(name)
        .email(username)
        .sex(sex)
        .hashPassword(password)
        .role(User.Role.VISITOR)
        .build();
  }
}
