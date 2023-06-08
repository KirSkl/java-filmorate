package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

@Component("DaoImpl")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    JdbcTemplate jdbcTemplate;
    @Override
    public Collection<User> findAllUsers() {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void removeUser(Long id) {

    }

    @Override
    public User getUserById(Long id) {
        return null;
    }
}
