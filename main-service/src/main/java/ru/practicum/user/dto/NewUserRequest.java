package ru.practicum.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class NewUserRequest {
    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "email не может быть пустым или слишком большим")
    private String email;

    @NotBlank
    @Size(min = 2, max = 250, message = "Имя не может быть пустым или слишком большим")
    private String name;
}
