package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, this::createGenre);
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, this::createGenre, id);
        if (genres.size() != 1) {
            throw new NotFoundException(String.format("Жанр с id %s не единственный", id));
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getGenreListByFilmId(Integer filmId) {
        String sqlQuery = "SELECT * FROM genres WHERE id IN (SELECT genre_id FROM film_genres WHERE film_id = ?)";

        return jdbcTemplate.query(sqlQuery, this::createGenre, filmId);
    }

    @Override
    public void setFilmsGenres(Integer filmId, List<Genre> genres) {
        String sqlQueryCleanFilmsGenres = "DELETE FROM film_genres WHERE film_id = ?";
        String sqlQuerySetGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQueryCleanFilmsGenres, filmId);

        if (genres != null) {
            for (Genre genre : Set.copyOf(genres)) {
                jdbcTemplate.update(sqlQuerySetGenres, filmId, genre.getId());
            }
        }
    }


    private Genre createGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
