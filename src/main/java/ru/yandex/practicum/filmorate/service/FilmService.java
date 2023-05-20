package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private InMemoryFilmStorage filmStorage;
    private InMemoryUserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }
    public void addLike(Long idFilm, Long idUser) {
        if (!filmStorage.getFilms().containsKey(idFilm)) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        if (!userStorage.getUsers().containsKey(idUser)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        filmStorage.getFilms().get(idFilm).getLikes().add(idUser);
    }

    public void deleteLike(Long idFilm, Long idUser) {
        if (!filmStorage.getFilms().containsKey(idFilm)) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        if (!userStorage.getUsers().containsKey(idUser)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        filmStorage.getFilms().get(idFilm).getLikes().remove(idUser);
    }

    public List<Film> get10MostPopularFilms() {
        return filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparingInt(o -> o.getLikes().size())).limit(10L)
                .collect(Collectors.toList());
    }
}
