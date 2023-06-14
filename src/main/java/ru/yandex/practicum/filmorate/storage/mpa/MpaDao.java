package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;

public interface MpaDao {
    Collection<MPARating> getAllRatings();

    MPARating getMpaById(int id);
}
