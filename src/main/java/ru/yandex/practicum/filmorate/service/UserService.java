package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    List<User> getAll();

    User update(User user);

    User getUserById(int id);

    List<User> getFriends(int id);

    List<User> getCommonFriends(int id, int otherId);

    void addFriend(int id, int userId);

    void removeFriend(int id, int userId);
}
