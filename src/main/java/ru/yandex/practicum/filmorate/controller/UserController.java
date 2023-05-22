package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        Validator.validateID(id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsOfUser(@PathVariable Long id) {
        Validator.validateID(id);
        return userService.getFriendsOfUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        Validator.validateID(id);
        Validator.validateID(otherId);
        return userService.showCommonFriends(id, otherId);
    }
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос POST /users - добавление пользователя");
        Validator.validateUser(user);
        userService.createUser(user);
        log.info("Пользователь добавлен");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT /users - обновление пользователя");
        Validator.validateUser(user);
        userService.updateUser(user);
        log.info("Пользователь обновлен");
        return user;
    }
    @PutMapping("{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        Validator.validateID(id);
        Validator.validateID(friendId);
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping
    public void removeUser(@RequestBody Long id) {
        log.info("Получен запрос DELETE /users - удаление пользователя");
        userService.removeUser(id);
        log.info("Пользователь удален");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        Validator.validateID(id);
        Validator.validateID(friendId);
        userService.removeFromFriends(id, friendId);
    }
}
