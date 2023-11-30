package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.LikeFilmRepository;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final LikeFilmRepository likeFilmRepository;
    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("filmDbRepository") FilmRepository filmRepository,
                       LikeFilmRepository likeFilmRepository, UserService userService) {
        this.filmRepository = filmRepository;
        this.likeFilmRepository = likeFilmRepository;
        this.userService = userService;
    }

    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmRepository.addFilm(film);
    }

    public Film getFilmById(int id) {
        try {
            return filmRepository.getFilmById(id)
                    .orElseThrow(() -> new NotExistObjectException("Film with id " + id + " does not exist."));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistObjectException("Film with id " + id + " does not exist.");
        }
    }

    public Film updateFilm(Film film) {
        return filmRepository.updateFilm(film);
    }

    public void deleteFilmById(int id) {
        filmRepository.deleteFilmById(id);
    }

    public void addLike(int idFilm, int idUser) {
        likeFilmRepository.addLike(idFilm, idUser);
    }

    public void removeLike(int idFilm, int idUser) {
        likeFilmRepository.removeLike(idFilm, idUser);
    }

    public List<Film> getMostNLikedFilms(int countFilms) {
        return filmRepository.getMostNLikedFilms(countFilms);
    }
}
