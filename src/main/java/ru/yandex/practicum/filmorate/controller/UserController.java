package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> userStorage = new HashMap<>();
    private int generatedId = 1;

    @GetMapping("/users")
    public Collection<User> getUsers() {
        log.info("Получение всех пользователей");
        return userStorage.values();
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        if (user != null) {
            log.info("Создание пользователя {}", user.getName());
            validateUser(user);
            user.setId(generatedId);
            userStorage.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User newUser) {
        log.info("Обновление пользователя {}", newUser.getId());
        if (userStorage.containsKey(newUser.getId())) {
            validateUser(newUser);
            User oldUser = userStorage.get(newUser.getId());
            oldUser.setLogin(newUser.getLogin());
            if (newUser.getName().isEmpty()) {
                oldUser.setName(newUser.getLogin());
            }
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            oldUser.setEmail(newUser.getEmail());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с таким id не найден");
    }

    void validateUser(User user) {
        log.info("Начало валидации для {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private int getNextId() {
        return generatedId++;
    }
}

