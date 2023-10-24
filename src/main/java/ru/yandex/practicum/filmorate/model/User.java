package ru.yandex.practicum.filmorate.model;

import  javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Login cannot be empty")
    @Pattern(regexp = "^\\S+$", message = "Login cannot contain spaces")
    private String login;

//    @NotNull(message = "Name can't be null")
    private String name;

    @Past(message = "Birthday cannot be in the future")
    private LocalDate birthday;
}
