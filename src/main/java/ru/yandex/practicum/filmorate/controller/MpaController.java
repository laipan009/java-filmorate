package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.services.GenreService;
import ru.yandex.practicum.filmorate.services.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping()
    public List<Mpa> getAllMpa() {
        log.info("GET request received to receive all mpa");
        return mpaService.getAllMpa();
    }


    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        log.info("GET request received to receive mpa by id=" + id);
        return mpaService.getMpaById(id);
    }

}
