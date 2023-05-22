package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    
    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
        filmStorage.getFilmById(idFilm).addLike(idUser);
    }

    public void deleteLike(Long idFilm, Long idUser) {
        filmStorage.getFilmById(idFilm).deleteLike(idUser);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted(Comparator.comparingInt(o -> -o.getRate())).limit(count)
                .collect(Collectors.toList());
    }
}
