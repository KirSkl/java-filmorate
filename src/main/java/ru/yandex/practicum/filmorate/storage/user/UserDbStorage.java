package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("UserDaoImpl")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> findAllUsers() {
        Collection<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users");
        while (userRows.next()) {
            User user = getUser(userRows);
            users.add(user);
        }
        return users;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(this.UserToMap(user)).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        final String sqlIsExists = "select count(*) From users WHERE user_id = ?";
        if (jdbcTemplate.queryForObject(sqlIsExists, Integer.class, user.getId()) == 0) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        String sqlQuery = "update users set " +
                "name = ?, email = ?, login = ?, birthday = ? " +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public void removeUser(Long id) {
        final String sqlIsExists = "select count(*) From users WHERE user_id = ?";
        if (jdbcTemplate.queryForObject(sqlIsExists, Integer.class, id) == 0) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        String sqlQuery = "delete from users where user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public User getUserById(Long id) {
        final String sqlIsExists = "select count(*) From users WHERE user_id = ?";
        if (jdbcTemplate.queryForObject(sqlIsExists, Integer.class, id) == 0) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select * from users where user_id = ?", id);
        User user = null;
        if (userRows.next()) {
            user = getUser(userRows);
        }
        return user;
    }

    @Override
    public List<User> getListFriends(Long id) {
        List<User> friends = new ArrayList<>();
        for (Long friendId : getIdsFriends(id)) {
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet(
                    "select * from users where user_id = ?", friendId);
            if (friendRows.next()) {
                friends.add(getUser(friendRows));
            }
        }
        return friends;
    }

    @Override
    public void addToFriends(Long idOfferor, long idAcceptor) {
        new SimpleJdbcInsert(jdbcTemplate).withTableName("friends").
                execute(this.friendsToMap(idOfferor, idAcceptor));
    }

    @Override
    public void removeFromFriends(Long id1, Long id2) {
        String sqlQuery = "delete from friends where offeror_id = ? and acceptor_id = ?";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    @Override
    public List<User> getCommonFriends(Long idFirstFriend, Long idSecondFriend) {
        List<User> commonFriends = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(
                "SELECT DISTINCT * FROM users WHERE user_id IN "
                        + "(SELECT acceptor_id FROM friends WHERE offeror_id = ? )"
                        + "AND user_id in"
                        + "(SELECT acceptor_id FROM friends WHERE offeror_id = ?) ",
                idFirstFriend, idSecondFriend);
        while (sqlRowSet.next()) {
            commonFriends.add(getUser(sqlRowSet));
        }
        return commonFriends;
    }

    private Set<Long> getIdsFriends(Long id) {
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(
                "select * from friends where offeror_id = ?", id);
        Set<Long> friends = new HashSet<>();
        while (friendsRows.next()) {
            friends.add(friendsRows.getLong("acceptor_id"));
        }
        return friends;
    }

    private User getUser(SqlRowSet userRows) {
        User user = new User(
                userRows.getLong("user_id"),
                userRows.getString("email"),
                userRows.getString("login"),
                userRows.getString("name"),
                userRows.getDate("birthday").toLocalDate()
        );
        user.setFriends(getIdsFriends(user.getId()));
        return user;
    }

    public Map<String, Object> UserToMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("birthday", user.getBirthday());
        return values;
    }

    public Map<String, Object> friendsToMap(Long idOfferor, Long idAcceptor) {
        Map<String, Object> values = new HashMap<>();
        values.put("offeror_id", idOfferor);
        values.put("acceptor_id", idAcceptor);
        return values;
    }
}
