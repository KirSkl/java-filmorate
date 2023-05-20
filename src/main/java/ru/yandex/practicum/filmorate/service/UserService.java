package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Long idFirstFriend, Long idSecondFriend) {
        userStorage.getUsers().get(idFirstFriend).getFriends().add(idSecondFriend);
        userStorage.getUsers().get(idSecondFriend).getFriends().add(idSecondFriend);
    }

    public void removeFromFriends(Long idFirstFriend, Long idSecondFriend) {
        userStorage.getUsers().get(idFirstFriend).getFriends().remove(idSecondFriend);
        userStorage.getUsers().get(idSecondFriend).getFriends().remove(idSecondFriend);
    }

    public List<User> showCommonFriends(Long idFirstFriend, Long idSecondFriend) {
        return userStorage.getUsers().values().stream()
                .filter(o -> o.getFriends().contains(idFirstFriend) && o.getFriends().contains(idSecondFriend))
                .collect(Collectors.toList());
    }
}
