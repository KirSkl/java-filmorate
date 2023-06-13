package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Data
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 0L;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.info("Неверный запрос PUT /users - пользователь не найден");
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void removeUser(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        users.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        return users.get(id);
    }

    @Override
    public List<User> getListFriends(Long id) {
        return null;
    }

    @Override
    public void addToFriends(Long idOfferor, long idAcceptor) {

    }

    @Override
    public void removeFromFriends(Long id1, Long id2) {

    }

    @Override
    public List<User> getCommonFriends(Long idFirstFriend, Long idSecondFriend) {
        return null;
    }


    private long getId() {
        return ++id;
    }
}