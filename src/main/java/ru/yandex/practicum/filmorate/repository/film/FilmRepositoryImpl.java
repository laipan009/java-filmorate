package ru.yandex.practicum.filmorate.repository.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
@Slf4j
public class FilmRepositoryImpl implements FilmRepository {
    private static int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        log.info("Getting all films");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        log.info("Adding film with id {}", film.getId());
        if (isFilmExists(film.getId())) {
            throw new RuntimeException("Film with same id=" + film.getId() + " already exist");
        }
        film.setId(id);
        films.put(id++, film);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        log.info("Getting film with id {}", id);
        if (!isFilmExists(id)) {
            throw new NotExistObjectException("Film with same id=" + id + " already not exist");
        }
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Updating film with id {}", id);
        if (!isFilmExists(film.getId())) {
            throw new NotExistObjectException("Film with same id=" + film.getId() + " already not exist");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getMostNLikedFilms(int countFilms) {
        return null;
    }

    @Override
    public void deleteFilmById(int id) {
        log.info("Deleting film with id {}", id);
        if (!isFilmExists(id)) {
            throw new NotExistObjectException("Film with same id=" + id + " already not exist");
        }
        films.remove(id);
    }

    public boolean isFilmExists(int id) {
        log.info("Checking film with id {} existence", id);
        return films.containsKey(id);
    }
}
