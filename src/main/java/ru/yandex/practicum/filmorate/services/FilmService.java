package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserService userService;

    @Autowired
    public FilmService(FilmRepository filmRepository, UserService userService) {
        this.filmRepository = filmRepository;
        this.userService = userService;
    }

    protected boolean isFilmLikedByUser(int filmId, int userId) {
        return Optional.ofNullable(filmRepository.getFilmById(filmId).getLikes())
                .map(set -> set.contains(userId))
                .orElse(false);
    }

    public List<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmRepository.addFilm(film);
    }

    public Film getFilmById(int id) {
        return filmRepository.getFilmById(id);
    }

    public Film updateFilm(Film film) {
        return filmRepository.updateFilm(film);
    }

    public void deleteFilmById(int id) {
        filmRepository.deleteFilmById(id);
    }

    public void addLike(int idFilm, int idUser) {
        Set<Integer> filmLikes = filmRepository.getFilmById(idFilm).getLikes();
        Set<Integer> likedFilms = userService.getUserById(idUser).getLikedFilms();
        if (isFilmLikedByUser(idFilm, idUser)) {
            throw new RuntimeException("Film already liked");
        }
        filmLikes.add(idUser);
        likedFilms.add(idFilm);
    }

    public void removeLike(int idFilm, int idUser) {
        Set<Integer> filmLikes = filmRepository.getFilmById(idFilm).getLikes();
        Set<Integer> likedFilms = userService.getUserById(idUser).getLikedFilms();
        if (!isFilmLikedByUser(idFilm, idUser)) {
            throw new RuntimeException("Film already not liked");
        }
        filmLikes.remove(idUser);
        likedFilms.remove(idFilm);
    }

    public List<Film> getMostNOr10LikedFilms(int countFilms) {
        int countTopLikedFilms = countFilms == 0 ? 10 : countFilms;
        return filmRepository.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(countTopLikedFilms)
                .collect(Collectors.toList());
    }
}
