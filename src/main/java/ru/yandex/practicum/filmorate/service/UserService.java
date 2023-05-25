package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
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
        userStorage.getUserById(idFirstFriend).getFriends().add(idSecondFriend);
        userStorage.getUserById(idSecondFriend).getFriends().add(idFirstFriend);
    }

    public void removeFromFriends(Long idFirstFriend, Long idSecondFriend) {
        userStorage.getUserById(idFirstFriend).getFriends().remove(idSecondFriend);
        userStorage.getUserById(idSecondFriend).getFriends().remove(idSecondFriend);
    }

    public List<User> getFriendsOfUser(Long id) {
        Set<Long> friendsIds =  userStorage.getUserById(id).getFriends();
        return userStorage.findAllUsers().stream().filter(user -> friendsIds.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public List<User> showCommonFriends(Long idFirstFriend, Long idSecondFriend) {
        return userStorage.findAllUsers().stream()
                .filter(user -> user.getFriends().contains(idFirstFriend)
                        && user.getFriends().contains(idSecondFriend))
                .collect(Collectors.toList());
    }
}
