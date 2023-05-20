package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {
    static InMemoryFilmStorage filmStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }
    @GetMapping
    public Collection<Film> findAllFilms() {
        return filmStorage.getFilms().values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films - добавление фильма");
        Validator.validateFilm(film);
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /films - обновление фильма");
        Validator.validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    @DeleteMapping
    public void removeFilm(@RequestBody Long id) {
        log.info("Получен запрос DELETE /films - удаление фильма");
        filmStorage.removeFilm(id);
        log.info("Фильм удален");
    }
}
