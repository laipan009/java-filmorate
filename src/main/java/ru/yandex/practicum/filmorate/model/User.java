package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Login cannot be empty")
    @Pattern(regexp = "^\\S+$", message = "Login cannot contain spaces")
    private String login;

    private String name;

    @Past(message = "Birthday cannot be in the future")
    private LocalDate birthday;

    private List<User> friends;
}
