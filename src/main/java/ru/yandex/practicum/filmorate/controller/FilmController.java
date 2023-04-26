package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    public final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.info("Ошибочный запрос POST /films - указана не положительная продолжительность");
            throw new ValidationException("Продолжительность должна быть положительной");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Ошибочный запрос POST /films - указана слишком ранняя дата релиза");
            throw new ValidationException("дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        log.info("Получен запрос POST /films - добавление фильма");
        films.put(film.getId(), film);
        return film;
    }
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.info("Ошибочный запрос POST /films - указана не положительная продолжительность");
            throw new ValidationException("Продолжительность должна быть положительной");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Ошибочный запрос POST /films - указана слишком ранняя дата релиза");
            throw new ValidationException("дата релиза должна быть не раньше 28 декабря 1895 года");
        }
        films.put(film.getId(), film);
        log.info("Получен запрос PUT /films - обновление фильма");
        return film;
    }
}
