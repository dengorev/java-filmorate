package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private int generatedId = 1;

    @Override
    public User save(User user) {
        if (user != null) {
            validate(user);
            user.setId(generatedId);
            getNextId();
        }
        return userStorage.save(user);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User update(User newUser) {
        User oldUser = userStorage.getUserById(newUser.getId());
        oldUser.setLogin(newUser.getLogin());
        validate(oldUser);
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

    private static void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private int getNextId() {
        return generatedId++;
    }
}
