package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component("UserDaoImpl")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert; //для добавления в таблицу users
    private final SimpleJdbcInsert simpleJdbcInsertFriend; //второй объект для добавления данных в таблицу friends

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        simpleJdbcInsertFriend = new SimpleJdbcInsert(jdbcTemplate).withTableName("friends");
    }

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
        user.setId(simpleJdbcInsert.executeAndReturnKey(this.userToMap(user)).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set " +
                "name = ?, email = ?, login = ?, birthday = ? " +
                "where user_id = ?";
        if (jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday(), user.getId()) != 1) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        return user;
    }

    @Override
    public void removeUser(Long id) {
        String sqlQuery = "delete from users where user_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) != 1) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
    }

    @Override
    public User getUserById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "select * from users where user_id = ?", id);
        if (!userRows.next()) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        User user = getUser(userRows);
        return user;
    }

    @Override
    public List<User> getListFriends(Long id) {
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet(
                "select u.* from users u "
                        + "inner join friends f on u.user_id = f.acceptor_id "
                        + "where offeror_id = ?", id);
        List<User> friends = new ArrayList<>();
            while (friendRows.next()) {
                friends.add(getUser(friendRows));
            }
        return friends;
    }

    @Override
    public void addToFriends(Long idOfferor, long idAcceptor) {
        simpleJdbcInsertFriend.execute(this.friendsToMap(idOfferor, idAcceptor));
    }

    @Override
    public void removeFromFriends(Long id1, Long id2) {
        String sqlQuery = "delete from friends where offeror_id = ? and acceptor_id = ?";
        if (jdbcTemplate.update(sqlQuery, id1, id2) != 1) {
            throw new UserNotFoundException("Друг с таким ID не найден");
        }
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

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("birthday", user.getBirthday());
        return values;
    }

    private Map<String, Object> friendsToMap(Long idOfferor, Long idAcceptor) {
        Map<String, Object> values = new HashMap<>();
        values.put("offeror_id", idOfferor);
        values.put("acceptor_id", idAcceptor);
        return values;
    }
}
