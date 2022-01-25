package ru.softwave.pool.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.softwave.pool.model.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String name;
    private String surname;
    private String email;
    private String role;

    public static UserDto from(User user){
        return UserDto.builder()
                .name(user.getName() ==null?"":user.getName())
                .surname(user.getSurname() ==null?"":user.getSurname())
                .email(user.getEmail() ==null?"":user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
