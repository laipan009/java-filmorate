package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    void deleteFilmById(int id);

    Optional<Film> getFilmById(int id);

    Film updateFilm(Film film);

    List<Film> getMostNLikedFilms(int countFilms);
}