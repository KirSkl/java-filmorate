package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос POST /users - добавление пользователя");
        Validator.validateUser(user);
        user.setId(getId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT /users - обновление пользователя");
        if (!users.containsKey(user.getId())) {
            log.info("Неверный запрос PUT /users - пользователь не найден");
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        Validator.validateUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь обновлен");
        return user;
    }

    private int getId() {
        return ++id;
    }
}
