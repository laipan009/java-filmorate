package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    private MpaRepository mpaRepository;

    @Autowired
    public MpaService(MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    public Mpa getMpaById(int id) {
        try {
            return mpaRepository.getMpaById(id)
                    .orElseThrow(() -> new NotExistObjectException("Mpa not exist by id=" + id));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistObjectException("Mpa not exist by id=" + id);
        }
    }


    public List<Mpa> getAllMpa() {
        return mpaRepository.getAllMpa();
    }
}
