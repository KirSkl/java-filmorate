package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Получен запрос GET /films - получить все фильмы");
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Получен запрос GET /films/id - получение фильма по ID");
        Validator.validateID(id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос GET /films/popular - получение списка самых популярных фильмов");
        return filmService.getMostPopularFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST /films - добавление фильма");
        if (film.getMpaRating() == null) {
            throw new MpaNotFoundException("Не указан рейтинг");
        }
        Validator.validateFilm(film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT /films - обновление фильма");
        Validator.validateFilm(film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос PUT /films/{id}/like/{userId} - поставить лайк фильму");
        Validator.validateID(id);
        Validator.validateID(userId);
        filmService.addLike(id, userId);
        log.info("Лайк поставлен!");
    }

    @DeleteMapping("{id}")
    public void removeFilm(@PathVariable Long id) {
        log.info("Получен запрос DELETE /films - удаление фильма");
        Validator.validateID(id);
        filmService.removeFilm(id);
        log.info("Фильм удален");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Получен запрос DELETE /films/{id}/like/{userId} - удалить лайк у фильма");
        Validator.validateID(id);
        Validator.validateID(userId);
        filmService.deleteLike(id, userId);
        log.info("Лайк удален!");
    }
}
