package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private int generatedId = 1;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User save(User user) {
        if (user != null) {
            log.info("Создание пользователя {}", user.getName());
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(generatedId);
            getNextId();
        }
        return userStorage.save(user);
    }

    @Override
    public List<User> getAll() {
        log.info("Получение всех пользователей");
        return userStorage.getAll();
    }

    @Override
    public User update(User newUser) {
        log.info("Обновление пользователя {}", newUser.getId());
        User oldUser = userStorage.getUserById(newUser.getId());
        oldUser.setLogin(newUser.getLogin());
        if (newUser.getName().isEmpty()) {
            oldUser.setName(newUser.getLogin());
        }
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setEmail(newUser.getEmail());
        return userStorage.update(oldUser);
    }

    @Override
    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<User> getFriends(int id) {
        User user = userStorage.getUserById(id);
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int id, int otherId) {
        log.info("Получение списка общих друзей пользователей c ID = {} и {}", id, otherId);
        Set<Integer> friendsOne = userStorage.getUserById(id).getFriends();
        Set<Integer> friendsTwo = userStorage.getUserById(otherId).getFriends();
        Set<Integer> collect = friendsOne.stream()
                .filter(friendsTwo::contains)
                .collect(Collectors.toSet());
        return collect.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public void addFriend(int id, int userId) {
        User userOne = getUserById(id);
        User userTwo = getUserById(userId);
        userOne.getFriends().add(userId);
        userTwo.getFriends().add(id);
    }

    @Override
    public void removeFriend(int id, int userId) {
        User userOne = getUserById(id);
        User userTwo = getUserById(userId);
        userOne.getFriends().remove(userId);
        userTwo.getFriends().remove(id);
    }

    private int getNextId() {
        return generatedId++;
    }
}
