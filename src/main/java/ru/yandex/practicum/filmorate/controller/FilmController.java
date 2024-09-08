package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private final Map<Integer, Film> filmStorage = new HashMap<>();
    private int generatedId = 1;

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.values();
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        log.info("Создание фильма {}", film.getName());
        validateFilm(film);
        film.setId(getNextId());
        filmStorage.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновление фильма с id={}", newFilm.getId());

        if (newFilm.getId() == 0) {
            throw new ValidationException("id не указан");
        }
        validateFilm(newFilm);
        Film oldFilm = filmStorage.get(newFilm.getId());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setName(newFilm.getName());
        return oldFilm;
    }

    void validateFilm(Film film) {
        log.info("Начало валидации для {}", film);
        if (film.getName() == null || film.getName().isEmpty()) {
            String msg = "Имя фильма не может быть пустым";
            log.error(msg);
            throw new ValidationException(msg);
        } else if (film.getDescription().length() > 200) {
            String msg = "Максимальная длинна описания 200 символов";
            log.error(msg);
            throw new ValidationException(msg);
        } else if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            String msg = "Дата релиза не может быть раньше 28 декабря 1895 года";
            log.error(msg);
            throw new ValidationException(msg);
        } else if (film.getDuration() < 0) {
            String msg = "Продолжительность фильма должна быть положительной";
            log.error(msg);
            throw new ValidationException(msg);
        }
    }

    private int getNextId() {
        return generatedId++;
    }
}
