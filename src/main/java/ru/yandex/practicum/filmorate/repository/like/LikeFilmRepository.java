package ru.yandex.practicum.filmorate.repository.like;

import java.util.Set;

public interface LikeFilmRepository {

    void addLike(int idFilm, int idUser);

    void removeLike(int idFilm, int idUser);

    Set<Integer> getFilmLikesById(int idFilm);
}