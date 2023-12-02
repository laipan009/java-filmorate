package ru.yandex.practicum.filmorate.repository.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.like.LikeFilmRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaDbRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Qualifier("filmDbRepository")
@Repository
@Slf4j
public class FilmDbRepository implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRepository genreRepository;
    private final MpaDbRepository mpaDbRepository;
    private final LikeFilmRepository likeFilmRepository;

    private Film mapToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaDbRepository.getMpaById(rs.getInt("mpa_id")).orElse(new Mpa()))
                .rate(likeFilmRepository.getFilmLikesById(rs.getInt("film_id")).size())
                .genres(genreRepository.getGenresByFilmId(rs.getInt("film_id")))
                .build();
    }

    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("film_name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate", film.getRate());
        values.put("mpa_id", film.getMpa().getId());
        return values;
    }

    @Autowired
    public FilmDbRepository(JdbcTemplate jdbcTemplate, GenreRepository genreRepository, MpaDbRepository mpaDbRepository,
                            LikeFilmRepository likeFilmRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreRepository = genreRepository;
        this.mpaDbRepository = mpaDbRepository;
        this.likeFilmRepository = likeFilmRepository;
    }

    @Override
    public List<Film> getAllFilms() {
        String query = "SELECT * FROM Film";
        log.info("All films returned from DB");
        return jdbcTemplate.query(query, this::mapToFilm);
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");
        Number key = simpleJdbcInsert.executeAndReturnKey(filmToMap(film));
        film.setId((Integer) key);

        if (!film.getGenres().isEmpty()) {
            String query = "INSERT INTO Genre_Film (film_id, genre_id) VALUES (?,?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(query, film.getId(), genre.getId());
            }
        }
        log.info("Film with ID {} saved.", film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String query = "SELECT * FROM Film WHERE film_id =?";
        log.info("Film with ID {} returned.", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, this::mapToFilm, id));
    }

    @Override
    public Film updateFilm(Film film) {
        String query = "UPDATE Film SET film_name=?, description=?, release_date=?, duration=?, rate =?, mpa_id=? " +
                "WHERE film_id=?";
        int filmId = film.getId();
        int updateResult = jdbcTemplate.update(query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                filmId);
        if (updateResult > 0) {
            log.info("Film with ID {} has been updated.", filmId);
        } else {
            throw new NotExistObjectException("Film not founded for update by ID=" + filmId);
        }

        if (!film.getGenres().isEmpty()) {
            String querySql = "DELETE FROM Genre_Film WHERE film_id =?";
            jdbcTemplate.update(querySql, filmId);
            String insertGenreQuery = "INSERT INTO Genre_Film (film_id, genre_id) VALUES (?, ?)";
            film.setGenres(film.getGenres()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList()));
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(insertGenreQuery, filmId, genre.getId());
            }
        } else {
            String querySql = "DELETE FROM Genre_Film WHERE film_id =?";
            jdbcTemplate.update(querySql, filmId);
        }
        return film;
    }

    @Override
    public void deleteFilmById(int id) {
        String query = "DELETE FROM Film WHERE film_id=?";
        int deleteResult = jdbcTemplate.update(query, id);
        if (deleteResult > 0) {
            log.info("Film with ID {} has been removed.", id);
        } else {
            log.info("Film with ID {} has not been deleted.", id);
        }
    }

    @Override
    public List<Film> getMostNLikedFilms(int countFilms) {
        String query = "SELECT f.*, COUNT(lf.user_id) AS likes " +
                "FROM Film f " +
                "LEFT JOIN Like_Film lf ON f.film_id = lf.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY likes DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Film film = mapToFilm(rs, rowNum);
            film.setRate(rs.getInt("likes")); // Assuming 'likes' is the alias for COUNT(lf.user_id)
            return film;
        }, countFilms);
    }
}
