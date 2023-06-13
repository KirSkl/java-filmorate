package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.ArrayList;
import java.util.Collection;

@Component
@AllArgsConstructor
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;
    @Override
    public Collection<MPARating> getAllRatings() {
        Collection<MPARating> ratings = new ArrayList<>();
        SqlRowSet rateRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa_rating");
        while (rateRows.next()) {
            ratings.add(getRating(rateRows));
        }
        return ratings;
    }

    @Override
    public MPARating getMpaById(int id) {
        final String sqlIsExists = "SELECT COUNT(*) FROM mpa_rating WHERE mpa_rate_id = ?";
        if (jdbcTemplate.queryForObject(sqlIsExists, Integer.class, id) == 0) {
            throw new MpaNotFoundException("Рейтинг с таким ID не найден");
        }
        SqlRowSet rateRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa_rating WHERE mpa_rate_id = ?", id);
        MPARating mpaRating = null;
        if (rateRows.next()) {
            mpaRating = getRating(rateRows);
        }
        return mpaRating;
    }

    private MPARating getRating(SqlRowSet rateRows) {
        return new MPARating(
                rateRows.getInt("mpa_rate_id"), rateRows.getString("name"));
    }
}
