package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.FilmDAO;
import ru.yandex.practicum.filmorate.DAO.UserDAO;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    protected final FilmDAO filmDAO;
    protected final UserService userService;
    protected final UserDAO userDAO;

    protected boolean isFilmExists(int idFilm) {
        return filmDAO.getAllFilms().stream()
                .anyMatch(film -> film.getId() == idFilm);
    }

    protected boolean isFilmLikedByUser(int filmId, int userId) {
        Optional<Set<Integer>> likes = Optional.ofNullable(filmDAO.getFilmById(filmId).getLikes());
        return likes.map(set -> set.contains(userId)).orElse(false);
    }

    @Autowired
    public FilmService(FilmDAO filmDAO, UserService userService, UserDAO userDAO) {
        this.filmDAO = filmDAO;
        this.userService = userService;
        this.userDAO = userDAO;
    }

    public void addLike(int idFilm, int idUser) {
        if (!userService.isUserExists(idUser)) {
            throw new NotExistObjectException("User not exist");
        }
        if (!isFilmExists(idFilm)) {
            throw new NotExistObjectException("Film not exist");
        }
        if (isFilmLikedByUser(idFilm, idUser)) {
            throw new RuntimeException("Film already liked");
        }
        Set<Integer> filmLikes = filmDAO.getFilmById(idFilm).getLikes();
        filmLikes.add(idUser);
        Set<Integer> likedFilms = userDAO.getUserById(idUser).getLikedFilms();
        likedFilms.add(idFilm);
    }

    public void removeLike(int idFilm, int idUser) {
        if (!userService.isUserExists(idUser)) {
            throw new NotExistObjectException("User not exist");
        }
        if (!isFilmExists(idFilm)) {
            throw new NotExistObjectException("Film not exist");
        }
        if (!isFilmLikedByUser(idFilm, idUser)) {
            throw new RuntimeException("Film already not liked");
        }
        Set<Integer> filmLikes = filmDAO.getFilmById(idFilm).getLikes();
        filmLikes.remove(idUser);
        Set<Integer> likedFilms = userDAO.getUserById(idUser).getLikedFilms();
        likedFilms.remove(idFilm);
    }

    public List<Film> getMostNOr10LikedFilms(int countFilms) {
        int countTopLikedFilms = countFilms == 0 ? 10 : countFilms;
        return filmDAO.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(countTopLikedFilms)
                .collect(Collectors.toList());
    }
}
