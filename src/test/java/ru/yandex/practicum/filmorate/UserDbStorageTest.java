package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    User user = User.builder()
            .id(1)
            .email("user1@email.ru")
            .name("user1")
            .login("user1login")
            .birthday(LocalDate.of(1990, 1, 1))
            .build();

    User user1 = User.builder()
            .id(2)
            .email("user2@email.ru")
            .name("user2")
            .login("user2login")
            .birthday(LocalDate.of(1994, 2, 1))
            .build();

    User user2 = User.builder()
            .id(3)
            .email("user3@email.ru")
            .name("user3")
            .login("user3login")
            .birthday(LocalDate.of(1970, 1, 1))
            .build();

    @Test
    public void testFindUserById() {
        userStorage.save(user);

        User savedUser = userStorage.getUserById(1);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void testFindAll() {
        userStorage.save(user);
        userStorage.save(user1);
        userStorage.save(user2);

        List<User> users = userStorage.getAll();

        Assertions.assertEquals(3, users.size());
    }

    @Test
    public void testCreate() {
        userStorage.save(user);

        User getUser = userStorage.getUserById(1);
        Assertions.assertEquals(user, getUser);
    }

    @Test
    public void testUpdate() {
        userStorage.save(user);

        user.setLogin("update");
        userStorage.update(user);

        User getUser = userStorage.getUserById(1);
        Assertions.assertEquals(user, getUser);
    }
}


