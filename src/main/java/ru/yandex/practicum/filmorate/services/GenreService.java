package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.List;

@Service
public class GenreService {
    private GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre getGenreById(int id) {
        try {
            return genreRepository.getById(id)
                    .orElseThrow(() -> new NotExistObjectException("Genre not exist by id=" + id));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistObjectException("Genre not exist by id=" + id);
        }

    }

    public List<Genre> getAll() {
        return genreRepository.getAll();
    }

    public List<Genre> getGenresByFilmId(int filmId) {
        return genreRepository.getGenresByFilmId(filmId);
    }
}