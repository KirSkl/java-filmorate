package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    public final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        Validator.validateFilm(film);
        log.info("Получен запрос POST /films - добавление фильма");
        film.setId(getId());
        films.put(film.getId(), film);
        return film;
    }
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if(!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        Validator.validateFilm(film);
        films.put(film.getId(), film);
        log.info("Получен запрос PUT /films - обновление фильма");
        return film;
    }
    private int getId() {
        return ++id;
    }
}
