package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void shouldReturnValidationExceptionCauseDurationIsZero() {
        Film filmZeroDuration = new Film("Пустышка",
                "Фильм с нулевой продолжительностью",
                LocalDate.of(1984, 01, 01),
                0, null, null);

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> Validator.validateFilm(filmZeroDuration));
        assertEquals("Продолжительность должна быть положительной", thrown.getMessage());
    }

    @Test
    void shouldReturnValidationExceptionCauseDurationIsNegative() {
        Film filmNegativeDuration = new Film("Негатив",
                "Фильм с негативной продолжительностью",
                LocalDate.of(1984, 01, 01),
                -100, null, null
        );

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> Validator.validateFilm(filmNegativeDuration));
        assertEquals("Продолжительность должна быть положительной", thrown.getMessage());
    }

    @Test
    void shouldReturnValidationExceptionCauseReleaseDateIsTooEarly() {
        Film filmTooEarly = new Film("Ранняя пташка",
                "Фильм, вдохновивший братьев Люмьер",
                LocalDate.of(1884, 01, 01),
                100, null, null
        );

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> Validator.validateFilm(filmTooEarly));
        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года", thrown.getMessage());
    }

    @Test
    void shouldReturnValidationExceptionCauseLoginContainsSpaces() {
        User userIncorrectLogin = new User("quentin@genius.com",
                "Female Feet",
                "Tarantino",
                LocalDate.of(1963, 03, 27));

        ValidationException thrown = assertThrows(ValidationException.class,
                () -> Validator.validateUser(userIncorrectLogin));
        assertEquals("Логин не должен содержать пробелов", thrown.getMessage());
    }

    @Test
    void shouldNotReturnExceptionWhenValidatesCorrectFilm() {
        Film filmCorrect = new Film("Нормальный фильм",
                "Можно скоротать вечерок",
                LocalDate.of(1984, 01, 01),
                100, null, null);

        assertDoesNotThrow(() -> Validator.validateFilm(filmCorrect));
    }

    @Test
    void shouldNotReturnExceptionWhenValidatesCorrectUser() {
        User userCorrect = new User("quentin@genius.com",
                "FemaleFeet",
                "Tarantino",
                LocalDate.of(1963, 03, 27));

        assertDoesNotThrow(() -> Validator.validateUser(userCorrect));
    }

    @Test
    void shouldDoesNameAsLogin() {
        User userNullName = new User("quentin@genius.com",
                "FemaleFeet",
                null,
                LocalDate.of(1963, 03, 27));

        assertEquals("FemaleFeet", userNullName.getName());
        assertDoesNotThrow(() -> Validator.validateUser(userNullName));
    }
}
