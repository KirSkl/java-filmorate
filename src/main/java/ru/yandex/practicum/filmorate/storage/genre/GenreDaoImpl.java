package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.ArrayList;
import java.util.Collection;

@Component
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao {

    JdbcTemplate jdbcTemplate;
    @Override
    public Collection<Genre> getAllGenres() {
        Collection<Genre> genres = new ArrayList<>();
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("select * from genres");
        while(genreRow.next()) {
            genres.add(getGenre(genreRow));
        }
        return genres;
    }

    @Override
    public Genre getGenreById(int id) {
        final String sqlIsExists = "SELECT COUNT(*) From genres WHERE genre_id = ?";
        if (jdbcTemplate.queryForObject(sqlIsExists, Integer.class, id) == 0) {
            throw new GenreNotFoundException("Жанр с таким ID не найден");
        }
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id = ?", id);
        Genre genre = null;
        if(genreRow.next()) {
            genre = getGenre(genreRow);
        }
        return genre;
    }

    private Genre getGenre(SqlRowSet genreRow) {
        return new Genre(genreRow.getInt("genre_id"),
                genreRow.getString("name"));
    }
}
