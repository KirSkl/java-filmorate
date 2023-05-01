package ru.yandex.practicum.filmorate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validator {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public static void validateFilm(Film film) {
        if (film.getDuration() <=0) {
            log.info("Ошибочный запрос POST /films - указана не положительная продолжительность");
            throw new ValidationException("Продолжительность должна быть положительной");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Ошибочный запрос POST /films - указана слишком ранняя дата релиза");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }
    public static void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.info("Ошибочный запрос POST /users - логин содержит пробелы");
            throw new ValidationException("Логин не должен содержать пробелов");
        }
    }
}
