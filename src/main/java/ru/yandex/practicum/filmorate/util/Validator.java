package ru.yandex.practicum.filmorate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationIDException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validator {

    private static final Logger log = LoggerFactory.getLogger(Validator.class);

    public static void validateFilm(Film film) {
        if (film.getDuration() <= 0) {
            log.info("Ошибочный запрос - указана не положительная продолжительность");
            throw new ValidationException("Продолжительность должна быть положительной");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Ошибочный запрос - указана слишком ранняя дата релиза");
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    public static void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            log.info("Ошибочный запрос - логин содержит пробелы");
            throw new ValidationException("Логин не должен содержать пробелов");
        }
    }

    public static void validateID(Long id) {
        if (id<1) {
            log.info("Ошибочный запрос - такой ID не может существовать");
            throw new ValidationIDException("ID меньше 1");
        }
    }
}
