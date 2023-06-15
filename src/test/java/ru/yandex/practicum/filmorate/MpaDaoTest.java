package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDaoImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
 class MpaDaoTest {
    private final MpaDaoImpl mpaDao;

    @Test
    void shouldReturnAllRatings() {
        List<MPARating> ratings = new ArrayList<>();
        ratings.add(new MPARating(1, "G"));
        ratings.add(new MPARating(2, "PG"));
        ratings.add(new MPARating(3, "PG-13"));
        ratings.add(new MPARating(4, "R"));
        ratings.add(new MPARating(5, "NC-17"));

        assertEquals(ratings, mpaDao.getAllRatings());
    }

    @Test
    void shouldReturnPG() {
        assertEquals(new MPARating(2, "PG"), mpaDao.getMpaById(2));
    }
}
