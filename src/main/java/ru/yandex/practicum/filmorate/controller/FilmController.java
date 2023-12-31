package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("GET request received to receive all films");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("GET request received to receive film by id=" + id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film addNewFilm(@Valid @RequestBody Film film) {
        log.info("POST request received to add film:" + film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("PUT request received to update film:" + film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("PUT request received to add like to film by id=" + id + " from user by id=" + userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("DELETE request received to remove like from film by id=" + id + " from user by id=" + userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostNOr10LikedFilms(@RequestParam(value = "count", defaultValue = "10") int count) {
        log.info("GET request received to receive TOP rated films");
        return filmService.getMostNLikedFilms(count);
    }
}
