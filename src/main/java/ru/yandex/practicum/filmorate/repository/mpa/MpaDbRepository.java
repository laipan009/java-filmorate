package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class MpaDbRepository implements MpaRepository {
    private final JdbcTemplate jdbcTemplate;

    private Mpa mapToMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }

    @Autowired
    public MpaDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        String query = "SELECT * FROM Mpa WHERE mpa_id=?";
        log.info("Mpa with ID {} returned.", id);
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, this::mapToMpa, id));
    }

    @Override
    public List<Mpa> getAllMpa() {
        String query = "SELECT mpa_id, mpa_name FROM Mpa";
        log.info("SELECT all Mpa from DB");
        return jdbcTemplate.query(query, this::mapToMpa);
    }
}
