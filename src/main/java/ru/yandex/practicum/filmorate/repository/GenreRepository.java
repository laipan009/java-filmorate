package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    Optional<Genre> getById(Integer id);

    List<Genre> getAll();

    List<Genre> getGenresByFilmId(int film_Id);
}
