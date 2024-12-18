package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::createUser);
    }

    @Override
    public User save(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return getUserById(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(Integer id) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::createUser, id);
        if (users.size() != 1) {
            throw new NotFoundException(String.format("Пользователя c id %s не существует", id));
        }
        return users.get(0);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String sqlQuery = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ?)";

        return jdbcTemplate.query(sqlQuery, this::createUser, id);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";

        if (friendshipExists(id, friendId)) {
            throw new ValidationException("Дружба уже существует");
        }
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void removeFriend(Integer id, Integer friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ? " +
                "INTERSECT SELECT friend_id FROM friends WHERE user_id = ?)";

        return jdbcTemplate.query(sqlQuery, this::createUser, id, otherId);
    }

    private User createUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    private boolean friendshipExists(Integer userId, Integer friendId) {
        String sqlQuery = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        return sqlRowSet.next();
    }
}
