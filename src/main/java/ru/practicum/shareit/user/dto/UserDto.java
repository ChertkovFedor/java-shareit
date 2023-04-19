package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    @Size(max = 50)
    private String name;
    @NotNull(message = "The user must have an email")
    @Email(message = "Email is incorrect")
    private String email;
}
