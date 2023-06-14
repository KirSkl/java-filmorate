package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;
    JdbcTemplate jdbcTemplate;

    private final User user1 = new User(
            "first@user.com", "first", "perviy",
            LocalDate.of(1996, 10, 23));
    private final User user2 = new User(
            "second@user.com", "second", "vtoroy",
            LocalDate.of(2000, 1, 20));
    private final User user3 = new User(
            "third@user.com", "third", "tretiy",
            LocalDate.of(1967, 3, 17));

    @Test
    public void shouldCreateAndReturnUserWithCorrectId() {
        assertEquals(1L, userStorage.addUser(user1).getId());
        assertEquals(2L, userStorage.addUser(user2).getId());

        user1.setFriends(new HashSet<Long>());
        user2.setFriends(new HashSet<Long>());

        assertEquals(user1, userStorage.getUserById(1L));
        assertEquals(user2, userStorage.getUserById(2L));
        }

    @Test
    public void shouldReturnCorrectUserById() {
        userStorage.addUser(user1);
        user1.setFriends(new HashSet<Long>());

        assertEquals(user1, userStorage.getUserById(1L));
    }

    @Test
    public void shouldReturnCollectionOfUsers() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);

        user1.setFriends(new HashSet<Long>());
        user2.setFriends(new HashSet<Long>());
        Collection<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        assertEquals(users, userStorage.findAllUsers());
    }

    @Test
    public void shouldReturnUpdatedUser() {
        userStorage.addUser(user1);
        user2.setId(1L);
        userStorage.updateUser(user2);
        user2.setFriends(new HashSet<Long>());

        assertEquals(user2, userStorage.getUserById(1L));
    }

    @Test
    public void shouldRemoveUser() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.removeUser(1L);
        user2.setFriends(new HashSet<Long>());
        Collection<User> users = new ArrayList<>();
        users.add(user2);

        assertEquals(users, userStorage.findAllUsers());
    }

    @Test
    public void shouldAddToFriend() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);

        userStorage.addToFriends(user1.getId(), user2.getId());
        Set<Long> friendsUser1 = new HashSet<>();
        friendsUser1.add(user2.getId());
        user1.setFriends(friendsUser1);

        assertEquals(user1.getFriends(), userStorage.getUserById(1L).getFriends());
    }

    @Test
    public void shouldReturnListOfFriends() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);

        userStorage.addToFriends(user1.getId(), user2.getId());
        Collection<User> friends = new ArrayList<>();
        user2.setFriends(new HashSet<Long>());
        friends.add(user2);

        assertEquals(friends, userStorage.getListFriends(1L));
    }

    @Test
    public void shouldDeleteFromFriends() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);

        userStorage.addToFriends(user1.getId(), user2.getId());
        userStorage.addToFriends(user1.getId(), user3.getId());

        Collection<User> friends = new ArrayList<>();
        user3.setFriends(new HashSet<Long>());
        friends.add(user3);

        userStorage.removeFromFriends(user1.getId(), user2.getId());

        assertEquals(friends, userStorage.getListFriends(1L));
    }

    @Test
    public void shouldReturnCommonFriend() {
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);

        userStorage.addToFriends(user2.getId(), user1.getId());
        userStorage.addToFriends(user3.getId(), user1.getId());

        Collection<User> friends = new ArrayList<>();
        user1.setFriends(new HashSet<Long>());
        friends.add(user1);

        assertEquals(friends, userStorage.getCommonFriends(user2.getId(), user3.getId()));
    }
    }


