package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Film validFilm;
    private Film film2;

    @BeforeEach
    void setUp() {
        validFilm = new Film();
        validFilm.setId(1);
        validFilm.setName("Test Film");
        validFilm.setDescription("Test Description");
        validFilm.setReleaseDate(LocalDate.now());
        validFilm.setDuration(120);

        film2 = new Film();
        film2.setId(2);
        film2.setName("Film2");
        film2.setDescription("Description2");
        film2.setReleaseDate(LocalDate.of(2021, 1, 1));
        film2.setDuration(130);
    }

    @Test
    void testAddNewFilmWhenValidFilmThenReturnCreated() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isOk());
    }

    @Test
    void testAddNewFilmWhenNameIsEmptyThenReturnBadRequest() throws Exception {
        validFilm.setName("");

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddNewFilmWhenDescriptionIsTooLongThenReturnBadRequest() throws Exception {
        validFilm.setDescription("A".repeat(201));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddNewFilmWhenReleaseDateIsBefore1895ThenReturnBadRequest() throws Exception {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 27));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddNewFilmWhenDurationIsNegativeThenReturnBadRequest() throws Exception {
        validFilm.setDuration(-1);

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddNewFilmWhenEmptyBodyThenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateFilmWhenValidFilmThenReturnFilm() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(validFilm)));
    }

    @Test
    public void testGetFilmsWhenFilmsExistThenReturnFilms() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validFilm)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(validFilm)));

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film2)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film2)));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(validFilm, film2))));
    }
}