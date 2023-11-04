package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.FilmDAO;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FilmDAOImpl implements FilmDAO {
    private static int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        if (films.containsKey(film.getId())) {
            throw new RuntimeException("Film with same id=" + film.getId() + " already exist");
        }
        film.setId(id);
        films.put(id++, film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new NotExistObjectException("Film with same id=" + id + " already not exist");
        }
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.get(film.getId()) == null) {
            throw new NotExistObjectException("Film with same id=" + film.getId() + " already not exist");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film deleteFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new NotExistObjectException("Film with same id=" + id + " already not exist");
        }
        return films.remove(id);
    }
}
