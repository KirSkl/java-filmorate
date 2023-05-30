package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAllUsers();

    User addUser(User user);

    User updateUser(User user);

    void removeUser(Long id);

    User getUserById(Long id);
}
