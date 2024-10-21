package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private final GenreStorage genreStorage;

    private final LikeStorage likeStorage;

    private final MpaStorage mpaStorage;

    @Override
    public Film save(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        List<Mpa> allMpa = mpaStorage.getAllMpa();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            Integer mpaId = film.getMpa().getId();
            if (allMpa.size() < mpaId) {
                throw new ValidationException("Не существующий рейтинг");
            }
            stmt.setInt(5, mpaId);
            return stmt;
        }, keyHolder);

        Integer filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        try {
            genreStorage.setFilmsGenres(filmId, film.getGenres());
        } catch (RuntimeException exception) {
            throw new ValidationException("Ошибка при добавлении жанра");
        }
        genreStorage.setFilmsGenres(filmId, film.getGenres());
        return getFilmById(filmId);
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, m.id AS mpa_id, m.name AS mpa_name, g.id AS genre_id, g.name AS genre_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, m.id, m.name, g.id, g.name";
        return jdbcTemplate.query(sqlQuery, this::createFilm);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id =? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        genreStorage.setFilmsGenres(film.getId(), film.getGenres());
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, m.id AS mpa_id, m.name AS mpa_name, g.id AS genre_id, g.name AS genre_name " +
                "FROM films f " +
                "LEFT JOIN mpa m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genres fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "WHERE f.id = ? " +
                "GROUP BY f.id, f.name, f.description, f.releaseDate, f.duration, m.id, m.name, g.id, g.name";

        List<Film> films = jdbcTemplate.query(sqlQuery, this::createFilm, id);
        if (films.size() != 1) {
            throw new NotFoundException(String.format("Фильм c id %s не существует", id));
        }
        return films.get(0);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        getFilmById(filmId);

        likeStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        List<Film> films = new ArrayList<>();

        for (Integer id : likeStorage.getPopular(count)) {
            films.add(getFilmById(id));
        }
        return films;
    }

    private List<Film> createFilm(ResultSet rs) throws SQLException {
        ResultSetExtractor<List<Film>> resultSetExtractor = rs1 -> {
            Map<Integer, Film> list = new HashMap<>();
            while (rs1.next()) {
                if (list.containsKey(rs1.getInt("id"))) {
                    list.get(rs1.getInt("id")).getGenres().add(Genre.builder()
                            .id(rs1.getInt("genre_id"))
                            .name(rs1.getString("genre_name"))
                            .build());
                } else {
                    Film film = Film.builder()
                            .id(rs1.getInt("id"))
                            .name(rs1.getString("name"))
                            .description(rs1.getString("description"))
                            .releaseDate(rs1.getDate("releaseDate").toLocalDate())
                            .duration(rs1.getInt("duration"))
                            .mpa(Mpa.builder()
                                    .id(rs1.getInt("mpa_id"))
                                    .name(rs1.getString("mpa_name"))
                                    .build())
                            .genres(new ArrayList<>())
                            .build();

                    if (rs1.getInt("genre_id") != 0) {
                        film.getGenres().add(Genre.builder()
                                .id(rs1.getInt("genre_id"))
                                .name(rs1.getString("genre_name"))
                                .build());
                    }

                    list.put(film.getId(), film);
                }
            }
            return new ArrayList<>(list.values());
        };
        return resultSetExtractor.extractData(rs);
    }
}
