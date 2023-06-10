package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("UserDaoImpl")UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        return userStorage.addUser(user);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void removeUser(Long id) {
        userStorage.removeUser(id);
    }

    public void addToFriends(Long idFirstFriend, Long idSecondFriend) {
        userStorage.addToFriends(idFirstFriend, idSecondFriend);
    }

    public void removeFromFriends(Long idFirstFriend, Long idSecondFriend) {
        userStorage.removeFromFriends(idFirstFriend, idSecondFriend);
    }

    public List<User> getFriendsOfUser(Long id) {
        return userStorage.getListFriends(id);
    }

    public List<User> showCommonFriends(Long idFirstFriend, Long idSecondFriend) {
        return userStorage.getCommonFriends(idFirstFriend, idSecondFriend);
    }
}
