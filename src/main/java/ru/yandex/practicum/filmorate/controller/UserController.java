package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getUsers() {
        log.info("Получение всех пользователей");
        return userService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создание пользователя");
        return userService.save(user);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User newUser) {
        log.info("Обновление пользователя");
        return userService.update(newUser);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable("id") int id) {
        log.info("Получение друзей пользователя");
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getCommonFriends(@PathVariable("id") int id, @PathVariable("otherId") int otherId) {
        log.info("Получение общих друзей пользователя");
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable("id") int id, @PathVariable("friendId") int userId) {
        log.info("Добавление друга");
        userService.addFriend(id, userId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable("id") int id, @PathVariable("friendId") int userId) {
        log.info("Удаление друга");
        userService.removeFriend(id, userId);

    }
}

