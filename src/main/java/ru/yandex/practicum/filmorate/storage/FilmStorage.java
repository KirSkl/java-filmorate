package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAllFilms();

    Film addFilm(Film film);

    void removeFilm(Long id);
  
    Film updateFilm(Film film);
  
    Film getFilmById(Long id);
}
