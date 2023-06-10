package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component("FilmDaoImpl")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> findAllFilms() {
        Collection<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films");
        while (filmRows.next()) {
            Film film = getFilm(filmRows);
            films.add(film);
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(this.FilmToMap(film)).longValue());
        updateGenres(film);
        return film;
    }

    @Override
    public void removeFilm(Long id) {
        final String sqlIsExists = "SELECT COUNT(*) From FILMS WHERE FILM_ID=?";
        if (jdbcTemplate.queryForObject(sqlIsExists, Integer.class, id) == 0) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        String sqlQuery = "delete from films where film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Film updateFilm(Film film) {
        final String sqlIsExists = "SELECT COUNT(*) From FILMS WHERE FILM_ID=?";
        if (jdbcTemplate.queryForObject(sqlIsExists, Integer.class, film.getId()) == 0) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_rate_id = ? " +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getId()
                , film.getMpaRating());
        updateGenres(film);
        return film;
    }

    @Override
    public Film getFilmById(Long id) {
        final String sqlIsExists = "SELECT COUNT(*) From FILMS WHERE FILM_ID=?";
        if (jdbcTemplate.queryForObject(sqlIsExists, Integer.class, id) == 0) {
            throw new FilmNotFoundException("Фильм с таким ID не найден");
        }
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "select * from films where film_id = ?", id);
        Film film = null;
        if(filmRows.next()) {
            film = getFilm(filmRows);
        }
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        new SimpleJdbcInsert(jdbcTemplate).withTableName("likes").execute(this.likeToMap(filmId, userId));
    }

    public Map<String, Object> FilmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_rate_id", film.getMpaRating());
        return values;
    }

    public Map<String, Object> likeToMap(Long filmId, Long userId) {
        Map<String, Object> values = new HashMap<>();
        values.put("film_id", filmId);
        values.put("user_id", userId);
        return values;
    }

    private Film getFilm(SqlRowSet filmRows) {
        Film film = new Film(filmRows.getLong("film_id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                filmRows.getDate("release_date").toLocalDate(),
                filmRows.getInt("duration"),
                filmRows.getInt("mpa_rate_id"),
                new ArrayList<Integer>());

        List<Integer> genres = new ArrayList<>();
        SqlRowSet genresRow = jdbcTemplate.queryForRowSet(
                "select genre_id from filmes_genres where film_id = ?", film.getId());
        while(genresRow.next()) {
            genres.add(genresRow.getInt("genre_id"));
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

    private Map<String, Object> FilmToGenresMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        for (Integer genreId : film.getGenres()) {
            values.put("film_id", film.getId());
            values.put("genre_id", genreId);
        }
        return values;
    }

    private void clearGenres(Film film) {
        jdbcTemplate.update("DELETE * FROM filmes_genres where FILM_ID = ?", film.getId());
    }

    private void updateGenres(Film film) {
        clearGenres(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("filmes_genres");
        simpleJdbcInsert.executeAndReturnKey(this.FilmToGenresMap(film));
    }
}
