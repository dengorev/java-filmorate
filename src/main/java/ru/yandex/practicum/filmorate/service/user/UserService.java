package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    List<User> getAll();

    User update(User user);

    User getUserById(Integer id);

    List<User> getFriends(Integer id);

    List<User> getCommonFriends(Integer id, Integer otherId);

    void addFriend(Integer id, Integer userId);

    void removeFriend(Integer id, Integer userId);
}
