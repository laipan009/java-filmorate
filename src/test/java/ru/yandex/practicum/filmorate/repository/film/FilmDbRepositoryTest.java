package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.like.LikeFilmDbRepository;
import ru.yandex.practicum.filmorate.repository.like.LikeFilmRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaDbRepository;
import ru.yandex.practicum.filmorate.repository.user.UserDbRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({LikeFilmDbRepository.class, UserDbRepository.class})
class FilmDbRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private MpaDbRepository mpaDbRepository;
    @Autowired
    private UserDbRepository userDbRepository;
    @Autowired
    private LikeFilmRepository likeFilmRepository;
    private FilmDbRepository filmDbRepository;
    private Film film;
    private Film typicalFilm;

    @BeforeEach
    void setUp() {
        filmDbRepository = new FilmDbRepository(jdbcTemplate, genreRepository, mpaDbRepository, likeFilmRepository);
        createFilms();
    }

    @Test
    void testGetAllFilmsWhenFilmsExistThenReturnFilms() {
        filmDbRepository.addFilm(film);

        Film filmFromDB = filmDbRepository.getAllFilms().get(0);

        assertThat(film).isEqualTo(filmFromDB);
    }

    @Test
    void testAddFilmWhenFilmAddedThenFilmInDatabase() {
        filmDbRepository.addFilm(film);

        Film retrievedFilm = filmDbRepository.getFilmById(film.getId()).get();

        assertThat(film).isEqualTo(retrievedFilm);
    }

    @Test
    void testUpdateFilmWhenFilmUpdatedThenFilmInDatabase() {
        Film addedFilm = filmDbRepository.addFilm(film);
        typicalFilm.setId(1);
        filmDbRepository.updateFilm(typicalFilm);

        Film retrievedFilm = filmDbRepository.getFilmById(addedFilm.getId()).get();

        assertThat(retrievedFilm).isEqualTo(typicalFilm);
    }

    @Test
    void testDeleteFilmByIdWhenFilmDeletedThenFilmNotInDatabase() {
        Film addedFilm = filmDbRepository.addFilm(film);

        filmDbRepository.deleteFilmById(addedFilm.getId());
        List<Film> allFilms = filmDbRepository.getAllFilms();

        assertTrue(allFilms.isEmpty());
    }

    @Test
    void testGetMostNLikedFilmsWhenFilmsExistThenReturnMostNLikedFilms() {
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(1992, 7, 9))
                .email("usew@mail.ru")
                .login("userok")
                .name("userok")
                .build();
        userDbRepository.addUser(user);
        filmDbRepository.addFilm(film);
        filmDbRepository.addFilm(typicalFilm);
        System.out.println(filmDbRepository.getAllFilms());

        likeFilmRepository.addLike(3, 1);

        List<Film> films = filmDbRepository.getMostNLikedFilms(2);

        assertTrue(films.get(0).getRate() == 1);
    }

    private void createFilms() {
        film = Film.builder()
                .name("Some Film")
                .releaseDate(LocalDate.of(2008, 11, 1))
                .description("This is description")
                .duration(120)
                .rate(0)
                .mpa(new Mpa())
                .genres(new ArrayList<>())
                .build();
        typicalFilm = Film.builder()
                .name("Typical Film")
                .releaseDate(LocalDate.of(2015, 8, 14))
                .description("Text about film")
                .duration(90)
                .rate(0)
                .mpa(new Mpa())
                .genres(new ArrayList<>())
                .build();
    }
}