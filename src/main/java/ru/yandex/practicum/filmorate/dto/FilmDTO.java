package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validators.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class FilmDTO {
    @NotBlank(message = "Title cannot be empty")
    @NotNull(message = "Title cannot be empty")
    private String name;

    @Size(max = 200, message = "Description cannot be more than 200 characters")
    private String description;

    @ValidReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Film duration must be positive")
    private Integer duration;
    private MpaDTO mpaDTO;
    private Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
}
