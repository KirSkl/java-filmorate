package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film addFilm(Film film);
    void removeFilm(Long id);
    Film updateFilm(Film film);

}
