package ru.yandex.practicum.filmorate.repository.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class GenreDbRepository implements GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    private Genre mapRowToGenre(ResultSet resultSet, int i) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }

    @Autowired
    public GenreDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getById(Integer id) {
        String query = "SELECT genre_id, genre_name FROM Genre WHERE genre_id = ?";
        log.info("SELECT request to DB genre by id=" + id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, this::mapRowToGenre, id));
    }

    @Override
    public List<Genre> getAll() {
        String query = "SELECT genre_id, genre_name FROM Genre";
        log.info("SELECT all genres from DB");
        return jdbcTemplate.query(query, this::mapRowToGenre);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String query = "SELECT g.genre_id, g.genre_name " +
                "FROM Genre g " +
                "JOIN Genre_Film gf ON g.genre_id = gf.genre_id " +
                "WHERE gf.film_id = ?";
        log.info("SELECT request to DB get genre by film_id=" + filmId);
        List<Genre> genres = jdbcTemplate.query(query, this::mapRowToGenre, filmId);
        return genres;
    }
}






