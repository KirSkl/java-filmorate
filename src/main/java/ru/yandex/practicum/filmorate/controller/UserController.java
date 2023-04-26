package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            log.info("Ошибочный запрос POST /users - логин содержит пробелы");
            throw new ValidationException("Логин не должен содержать пробелов");
        }
        if (user.getName().isBlank()) {
             user.setName(user.getLogin());
        }
        log.info("Получен запрос POST /users - добавление поьзователя");
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            log.info("Ошибочный запрос PUT /users - логин содержит пробелы");
            throw new ValidationException("Логин не должен содержать пробелов");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Получен запрос PUT /users - обновление пользователя");
        users.put(user.getId(), user);
        log.info("Пользователь обновлен");
        return user;
    }
}
