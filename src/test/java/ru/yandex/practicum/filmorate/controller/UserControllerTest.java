package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }

    @Test
    public void whenUserContainsEmailThenThrowValidationException() {
        User user = User.builder()
                .email("емейл")
                .name("имя")
                .login("логин")
                .birthday(LocalDate.of(1998, 5, 6))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userController.validateUser(user));
    }

    @Test
    public void whenUserLoginIsEmptyThenThrowValidationException() {
        User user = User.builder()
                .email("емейл@yandex")
                .name("имя")
                .login(" ")
                .birthday(LocalDate.of(1998, 5, 6))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userController.validateUser(user));
    }

    @Test
    public void whenUserBirthdayAfterNowThenThrowValidationException() {
        User user = User.builder()
                .email("емейл@yandex")
                .name("имя")
                .login("логин")
                .birthday(LocalDate.of(2054, 5, 6))
                .build();
        Assertions.assertThrows(ValidationException.class, () -> userController.validateUser(user));
    }
}
