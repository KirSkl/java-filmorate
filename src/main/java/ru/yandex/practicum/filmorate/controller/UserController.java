package ru.yandex.practicum.filmorate.controller;

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

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелов");
        }
        if (user.getName().isBlank()) {
             user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелов");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }
}
