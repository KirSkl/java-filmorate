package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.util.Validator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("Получен запрос GET /users - получить список пользователей");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Получен запрос POST /users/{id} - найти пользователя по ID");
        Validator.validateID(id);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsOfUser(@PathVariable Long id) {
        log.info("Получен запрос POST /users/{id}/friends - получить список друзей пользователя");
        Validator.validateID(id);
        return userService.getFriendsOfUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info(
                "Получен запрос POST /users/{id}/friends/common/{otherId} - получить список общих друзей пользователей");
        Validator.validateID(id);
        Validator.validateID(otherId);
        Validator.validateIDs(id, otherId);
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
        log.info("Получен запрос PUT /users{id}/friends/{friendId} - добавить пользователя в друзья");
        Validator.validateID(id);
        Validator.validateID(friendId);
        Validator.validateIDs(id, friendId);
        userService.addToFriends(id, friendId);
        log.info("Пользователю отправлена заявка на добавление в друзья");
    }

    @DeleteMapping("{id}")
    public void removeUser(@PathVariable Long id) {
        log.info("Получен запрос DELETE /users - удаление пользователя");
        Validator.validateID(id);
        userService.removeUser(id);
        log.info("Пользователь удален");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Получен запрос DELETE /users/{id}/friends/{friendId} - удаление пользователя из друзей");
        Validator.validateID(id);
        Validator.validateID(friendId);
        Validator.validateIDs(id, friendId);
        userService.removeFromFriends(id, friendId);
        log.info("Пользователь удален из друзей");
    }
}
