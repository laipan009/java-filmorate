package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDAO {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film getFilmById(int id);

    Film updateFilm(Film film);

    Film deleteFilmById(int id);
}