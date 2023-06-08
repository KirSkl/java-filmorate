package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component("DaoImpl")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> findAllFilms() {
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public void removeFilm(Long id) {

    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(Long id) {
        return null;
    }
}
