package ru.yandex.practicum.filmorate.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmServiceTest {

    @Autowired
    private FilmService filmService;

    private Film validFilm;
    private Film anotherOneValidFilm;
    private Film defaultFilm;
    private User user;
    private User someUser;

    @BeforeEach
    void setUp() {
        validFilm = createFilm(0, "Test Film", "Test Description", LocalDate.now(), 120);
        defaultFilm = createFilm(0, "Test Film2", "Test Description2", LocalDate.now(), 120);
        anotherOneValidFilm = createFilm(0, "Film3", "Description3", LocalDate.of(2021, 1, 1), 130);
        user = createUser(1, "test@test.com", "test", "Test User", LocalDate.of(1990, 1, 1));
        someUser = createUser(2, "test2@test.com", "test2", "Test User2", LocalDate.of(1992, 2, 2));
    }

    @Test
    void addLike_WhenTwoFilmsExistInDBThenUserAddLikesBothReturnTrue() {
        filmService.filmDAO.addFilm(validFilm);
        filmService.filmDAO.addFilm(defaultFilm);
        filmService.userService.userDAO.addUser(user);

        filmService.addLike(validFilm.getId(), user.getId());
        filmService.addLike(defaultFilm.getId(), user.getId());

        assertTrue(filmService.isFilmLikedByUser(validFilm.getId(), user.getId()));
        assertTrue(filmService.isFilmLikedByUser(defaultFilm.getId(), user.getId()));
    }

    @Test
    void removeLike_WhenTwoFilmsExistInDBThenUserAddLikesAndRemoveLikesBothReturnFalse() {
        filmService.filmDAO.addFilm(validFilm);
        filmService.filmDAO.addFilm(defaultFilm);
        filmService.userService.userDAO.addUser(user);
        filmService.addLike(validFilm.getId(), user.getId());
        filmService.addLike(defaultFilm.getId(), user.getId());

        filmService.removeLike(defaultFilm.getId(), user.getId());
        filmService.removeLike(validFilm.getId(), user.getId());

        assertFalse(filmService.isFilmLikedByUser(validFilm.getId(), user.getId()));
        assertFalse(filmService.isFilmLikedByUser(defaultFilm.getId(), user.getId()));
    }

    @Test
    void getMostNOr10LikedFilms_WhenExist3FilmsWith0LikesThen2UsersLikedOneFilmAndNoOneLikeTwoOtherReturn3SortedTopFilms() {
        filmService.filmDAO.addFilm(validFilm);
        filmService.filmDAO.addFilm(defaultFilm);
        filmService.filmDAO.addFilm(anotherOneValidFilm);
        filmService.userService.userDAO.addUser(user);
        filmService.userService.userDAO.addUser(someUser);
        filmService.addLike(validFilm.getId(), user.getId());
        filmService.addLike(validFilm.getId(), someUser.getId());

        List<Film> mostNOr10LikedFilms = filmService.getMostNOr10LikedFilms(20);

        assertThat(mostNOr10LikedFilms.get(0)).isEqualTo(validFilm);
    }

    private Film createFilm(int id, String name, String desc, LocalDate date, int duration) {
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription(desc);
        film.setReleaseDate(date);
        film.setDuration(duration);
        return film;
    }

    private User createUser(int id, String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }
}