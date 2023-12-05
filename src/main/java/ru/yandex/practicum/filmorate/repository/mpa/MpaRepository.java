package ru.yandex.practicum.filmorate.repository.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaRepository {
    Optional<Mpa> getMpaById(int id);

    List<Mpa> getAllMpa();
}