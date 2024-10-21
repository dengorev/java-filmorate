package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, MpaDbStorage::createMpa);
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sql = "SELECT * FROM mpa WHERE id = ?";
        List<Mpa> mpaList = jdbcTemplate.query(sql, MpaDbStorage::createMpa, id);
        if (mpaList.size() != 1) {
            throw new NotFoundException(String.format("Рейтинг с id %s не единственный", id));
        }
        return mpaList.get(0);
    }

    static Mpa createMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
