package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreDao {

    Collection<Genre> getAllGenres();

    Genre getGenreById(int id);
}