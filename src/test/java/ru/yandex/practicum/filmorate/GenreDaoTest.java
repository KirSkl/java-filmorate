package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDaoImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreDaoTest {
    private final GenreDaoImpl genreDao;

    @Test
    void shouldReturnAllGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1, "Комедия"));
        genres.add(new Genre(2, "Драма"));
        genres.add(new Genre(3, "Мультфильм"));
        genres.add(new Genre(4, "Триллер"));
        genres.add(new Genre(5, "Документальный"));
        genres.add(new Genre(6, "Боевик"));

        assertEquals(genres, genreDao.getAllGenres());
    }

    @Test
    void shouldReturnThriller() {
        assertEquals(new Genre(4, "Триллер"), genreDao.getGenreById(4));
    }
}
