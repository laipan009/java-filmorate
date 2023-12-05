package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MpaDbRepository mpaDbRepository;

    @BeforeEach
    void setUp() {
        mpaDbRepository = new MpaDbRepository(jdbcTemplate);

    }

    @Test
    void getById() {
        Optional<Mpa> byId = mpaDbRepository.getMpaById(1);
        assertThat(byId.get().getName()).isEqualTo("G");
    }

    @Test
    void getAll() {
        List<Mpa> all = mpaDbRepository.getAllMpa();

        assertThat(all.size()).isEqualTo(5);
        assertThat(all.get(0).getName()).isEqualTo("G");
        assertThat(all.get(4).getName()).isEqualTo("NC-17");
    }
}