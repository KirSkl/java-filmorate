package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {

    private Long id = 0L;
    private Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void removeFilm(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        films.remove(id);
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
    }

    @Override
    public void addLike(Long idFilm, Long idUser) {

    }

    private Long getId() {
        return ++id;
    }
}
