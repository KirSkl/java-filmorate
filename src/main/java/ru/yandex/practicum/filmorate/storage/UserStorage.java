package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> findAllUsers();

    User addUser(User user);

    User updateUser(User user);

    void removeUser(Long id);

    User getUserById(Long id);

    List<User> getListFriends(Long id);

    void addToFriends(Long idOfferor, long idAcceptor);

    void removeFromFriends(Long id1, Long id2);

    List<User> getCommonFriends(Long idFirstFriend, Long idSecondFriend);
}
