package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film addNewFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Film with same id=" + film.getId() + " already exist");
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }
}
