package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void removeFilm(Long id) {
        filmStorage.removeFilm(id);
    }

    public void addLike(Long idFilm, Long idUser) {
        filmStorage.getFilmById(idFilm).getLikes().add(idUser);
    }

    public void deleteLike(Long idFilm, Long idUser) {
        /*if (!filmStorage.getFilms().containsKey(idFilm)) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        if (!userStorage.getUsers().containsKey(idUser)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }*/
        filmStorage.getFilmById(idFilm).getLikes().remove(idUser);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparingInt(o -> o.getLikes().size())).limit(count)
                .collect(Collectors.toList());
    }
}
