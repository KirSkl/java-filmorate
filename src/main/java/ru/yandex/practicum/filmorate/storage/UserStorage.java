package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    void removeUser(int id);
}
