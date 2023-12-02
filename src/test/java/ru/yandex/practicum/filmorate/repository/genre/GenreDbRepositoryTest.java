package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private GenreDbRepository genreDbRepository;

    @BeforeEach
    void setUp() {
        genreDbRepository = new GenreDbRepository(jdbcTemplate);

    }

    @Test
    void getById() {
        Optional<Genre> byId = genreDbRepository.getById(1);
        assertThat(byId.get().getName()).isEqualTo("Комедия");
    }

    @Test
    void getAll() {
        List<Genre> all = genreDbRepository.getAll();

        assertThat(all.size()).isEqualTo(6);
        assertThat(all.get(0).getName()).isEqualTo("Комедия");
        assertThat(all.get(5).getName()).isEqualTo("Боевик");
    }
}