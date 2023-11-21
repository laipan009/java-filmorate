package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;

    private GenreType genreType;

    private RatingType ratingType;

    @NotBlank(message = "Title cannot be empty")
    @NotNull(message = "Title cannot be empty")
    private String name;

    @Size(max = 200, message = "Description cannot be more than 200 characters")
    private String description;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Film duration must be positive")
    private int duration;

    private Set<Integer> likes = new HashSet<>();
}
