package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long id = 0L;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
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
    public void removeUser(long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        users.remove(id);
    }

    private long getId() {
        return ++id;
    }
}
