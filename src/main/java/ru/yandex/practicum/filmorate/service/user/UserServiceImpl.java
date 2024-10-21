package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User update(User user) {
        userStorage.getUserById(user.getId());

        return userStorage.update(user);
    }

    public List<User> getFriends(Integer id) {
        userStorage.getUserById(id);

        return userStorage.getFriends(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);

        userStorage.addFriend(id, friendId);
    }

    public void removeFriend(Integer id, Integer friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);

        userStorage.removeFriend(id, friendId);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userStorage.getUserById(id);
        userStorage.getUserById(otherId);

        return userStorage.getCommonFriends(id, otherId);
    }
}
