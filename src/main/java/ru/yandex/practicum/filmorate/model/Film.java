package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.validators.ValidReleaseDate;

import java.time.LocalDate;

@Data
public class Film {
    @Positive(message = "id can't be negative")
    private int id;

    @NotBlank(message = "Title cannot be empty")
    @NotNull(message = "Title cannot be empty")
    private String name;

    @Size(max = 200, message = "Description cannot be more than 200 characters")
    private String description;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Film duration must be positive")
    private int duration;
}
