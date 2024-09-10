package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> filmStorage = new HashMap<>();
    private int generatedId = 1;

    @GetMapping("/films")
    public Collection<Film> getFilms() {
        log.info("Получение всех фильмов");
        return filmStorage.values();
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film != null) {
            log.info("Создание фильма {}", film.getName());
            film.setId(getNextId());
            filmStorage.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Обновление фильма с id={}", newFilm.getId());
        if (filmStorage.containsKey(newFilm.getId())) {
            Film oldFilm = filmStorage.get(newFilm.getId());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setName(newFilm.getName());
            return oldFilm;
        } else {
            throw new ValidationException("Фильм не найден");
        }
    }

    private int getNextId() {
        return generatedId++;
    }
}
