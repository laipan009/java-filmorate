package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film getFilmById(int id);

    Film updateFilm(Film film);

    void deleteFilmById(int id);

    boolean isFilmExists(int id);
}