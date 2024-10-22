package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    List<User> getAll();

    User update(User user);

    User getUserById(Integer id);

    List<User> getFriends(Integer id);

    void addFriend(Integer id, Integer friendId);

    void removeFriend(Integer id, Integer friendId);

    List<User> getCommonFriends(Integer id, Integer otherId);
}
