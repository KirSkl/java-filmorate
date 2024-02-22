package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.*;

@Component("FilmDaoImpl")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films")
                .usingGeneratedKeyColumns("film_id");
    }

    @Override
    public Collection<Film> findAllFilms() {
        Collection<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT * from films");
        while (filmRows.next()) {
            Film film = getFilm(filmRows);
            films.add(film);
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(simpleJdbcInsert.executeAndReturnKey(this.filmToMap(film)).longValue());
        updateGenres(film);
        return film;
    }

    @Override
    public void removeFilm(Long id) {
        String sqlQuery = "delete from films where film_id = ?";
        if (jdbcTemplate.update(sqlQuery, id) != 1) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_rate_id = ? " +
                "where film_id = ?";
        if (jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpaRating().getId(),
                film.getId()) != 1) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        updateGenres(film);
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(Long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT film_id, f.name AS title, description, "
                + "release_date, duration, f.mpa_rate_id, mr.name FROM films f "
                + "LEFT JOIN MPA_RATING mr ON f.MPA_RATE_ID = mr.MPA_RATE_ID "
                + "WHERE film_id = ?", id);
        if (!filmRows.next()) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        return getFilm(filmRows);
    }

    public void addLike(Long filmId, Long userId) {
        String sqlString = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlString, filmId, userId);
    }

    @Override
    public void deleteLike(Long idFilm, Long idUser) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        if (jdbcTemplate.update(sqlQuery, idFilm, idUser) != 1) {
            throw new FilmNotFoundException("Фильму с таким ID этот пользователь лайков не ставил");
        }
    }

    private Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_rate_id", film.getMpaRating().getId());
        return values;
    }

    private Film getFilm(SqlRowSet filmRows) {
        Film film = new Film(filmRows.getLong("film_id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                filmRows.getDate("release_date").toLocalDate(),
                filmRows.getInt("duration"),
                new MPARating(filmRows.getInt("mpa_rate_id"), filmRows.getString("name")),
                new HashSet<Genre>());

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(
                "select name from mpa_rating where mpa_rate_id = ?",
                film.getMpaRating().getId());
        if (mpaRows.next()) {
            film.getMpaRating().setName(mpaRows.getString("name"));
        }
        Set<Genre> genres = new HashSet<>();
        SqlRowSet genresRow = jdbcTemplate.queryForRowSet(
                "select * from films_genres fg left join genres g on fg.genre_id = g.genre_id where film_id = ?",
                film.getId());
        while (genresRow.next()) {
            genres.add(new Genre(genresRow.getInt("genre_id"),
                    genresRow.getString("name")));
        }
        film.setGenres(genres);

        SqlRowSet likesRows = jdbcTemplate.queryForRowSet(
                "select user_id from likes where film_id = ?", film.getId());
        Set<Long> likes = new HashSet<>();
        while (likesRows.next()) {
            likes.add(likesRows.getLong("user_id"));
        }
        film.setLikes(likes);
        return film;
    }

    private void clearGenres(Film film) {
        jdbcTemplate.update("DELETE FROM films_genres where FILM_ID = ?",
                film.getId());
    }

    private void updateGenres(Film film) {
        clearGenres(film);
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        for (Genre genre : film.getGenres()) {
            String sqlString = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlString, film.getId(), genre.getId());
        }
    }
}
