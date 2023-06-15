package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.Collections.EMPTY_SET;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final User user1 = new User(
            "first@user.com", "first", "perviy",
            LocalDate.of(1996, 10, 23));
    private final Genre genre = new Genre(1, "Комедия");
    private final Film film1 = new Film(
            "First", "first movie",
            LocalDate.of(1990, 1, 1), 100,
            new MPARating(1, "G"), Set.of(genre));
    private final Film film2 = new Film(
            "Second", "second movie",
            LocalDate.of(1980, 1, 1), 100,
            new MPARating(1, "G"), Set.of(genre));

    @Test
    void shouldCreateFilmAndReturnCorrectId() {
        assertEquals(1L, filmStorage.addFilm(film1).getId());

        assertEquals(film1, filmStorage.getFilmById(1L));
    }

    @Test
    void shouldReturnCorrectFilmById() {
        filmStorage.addFilm(film1);

        assertEquals(film1, filmStorage.getFilmById(1L));
    }

    @Test
    void shouldReturnCollectionOfFilm() {
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);

        assertEquals(List.of(film1, film2), filmStorage.findAllFilms());
    }

    @Test
    void shouldReturnUpdatedFilm() {
        filmStorage.addFilm(film1);
        film2.setId(1L);
        filmStorage.updateFilm(film2);

        assertEquals(film2, filmStorage.getFilmById(1L));
    }

    @Test
    void shouldDeleteFilm() {
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        filmStorage.removeFilm(film1.getId());

        assertEquals(List.of(film2), filmStorage.findAllFilms());
    }

    @Test
    void shouldAddAndDeleteLike() {
        userStorage.addUser(user1);
        filmStorage.addFilm(film1);

        filmStorage.addLike(film1.getId(), user1.getId());

        assertEquals(Set.of(user1.getId()), filmStorage.getFilmById(1L).getLikes());

        filmStorage.deleteLike(film1.getId(), user1.getId());

        assertEquals(EMPTY_SET, filmStorage.getFilmById(1L).getLikes());
    }

}
