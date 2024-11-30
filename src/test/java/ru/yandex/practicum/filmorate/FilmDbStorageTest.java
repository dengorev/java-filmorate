package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private  User user;
    private  User user1;
    private  User user2;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1)
                .email("user1@email.ru")
                .name("user1")
                .login("user1login")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        user1 = User.builder()
                .id(2)
                .email("user2@email.ru")
                .name("user2")
                .login("user2login")
                .birthday(LocalDate.of(1994, 2, 1))
                .build();

        user2 = User.builder()
                .id(3)
                .email("user3@email.ru")
                .name("user3")
                .login("user3")
                .birthday(LocalDate.of(1970, 1, 1))
                .build();
    }

    Mpa mpa1Full = Mpa.builder().id(4).name("R").build();
    Mpa mpa2Full = Mpa.builder().id(3).name("PG-13").build();

    Genre genre1 = Genre.builder().id(2).build();
    Genre genre1Full = Genre.builder().id(2).name("Драма").build();
    Genre genre2 = Genre.builder().id(1).build();
    Genre genre2Full = Genre.builder().id(1).name("Комедия").build();

    Film film = Film.builder()
            .id(1)
            .name("Опенгеймер")
            .description("История жизни американского физика-теоретика Роберта Оппенгеймера, " +
                    "который во времена Второй мировой войны руководил Манхэттенским проектом — " +
                    "секретными разработками ядерного оружия.")
            .releaseDate(LocalDate.of(2023, 7, 19))
            .duration(180)
            .mpa(mpa1Full)
            .genres(List.of(genre1))
            .build();

    Film film1 = Film.builder()
            .id(2)
            .name("Форрест Гамп")
            .description("С самого малолетства парень страдал от заболевания ног, соседские мальчишки дразнили его, " +
                    "но в один прекрасный день Форрест открыл в себе невероятные способности к бегу. ")
            .releaseDate(LocalDate.of(1994, 6, 23))
            .duration(142)
            .mpa(mpa2Full)
            .genres(List.of(genre2))
            .build();

    @Test
    public void createFilm() {
        filmStorage.save(film);
        film.setMpa(mpa1Full);
        film.setGenres(List.of(genre1Full));
        Film getFilm = filmStorage.getFilmById(1);

        assertThat(getFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void createFilmWithSeveralGenres() {
        film.setGenres(List.of(genre1, genre2));
        filmStorage.save(film);

        film.setMpa(mpa1Full);
        film.setGenres(List.of(genre2Full, genre1Full));
        Film getFilm = filmStorage.getFilmById(1);

        assertThat(getFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void findAll() {
        filmStorage.save(film);
        filmStorage.save(film1);

        List<Film> getFilms = filmStorage.getAll();

        film.setMpa(mpa1Full);
        film1.setMpa(mpa2Full);
        film1.setGenres(List.of(genre2Full));
        film.setGenres(List.of(genre1Full));

        Assertions.assertEquals(List.of(film, film1), getFilms);
    }

    @Test
    public void getFilmById() {
        filmStorage.save(film);
        film.setMpa(mpa1Full);
        film.setGenres(List.of(genre1Full));

        Film getFilm = filmStorage.getFilmById(1);
        Assertions.assertEquals(film, getFilm);
    }

    @Test
    public void update() {
        filmStorage.save(film);

        film.setName("Обновленный");

        filmStorage.update(film);
        Film getFilm = filmStorage.getFilmById(1);
        film.setMpa(mpa1Full);
        film.setGenres(List.of(genre1Full));

        Assertions.assertEquals(film, getFilm);
    }

    @Test
    public void getPopularFilm() {
        filmStorage.save(film);
        filmStorage.save(film1);
        userStorage.save(user);
        userStorage.save(user1);
        userStorage.save(user2);

        filmStorage.addLike(1, 1);
        filmStorage.addLike(2, 1);
        filmStorage.addLike(1, 2);
        filmStorage.addLike(1, 3);

        film.setMpa(mpa1Full);
        film1.setMpa(mpa2Full);

        film.setGenres(List.of(genre1Full));
        film1.setGenres(List.of(genre2Full));

        List<Film> getPopularFilm = filmStorage.getPopular(2);

        Assertions.assertEquals(List.of(film, film1), getPopularFilm);
    }
}


