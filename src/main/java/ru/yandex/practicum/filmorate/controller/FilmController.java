package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getFilms() {
        log.info("Получение всех фильмов");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable("id") Integer id) {
        log.info("Получение фильма по id");
        return filmService.getFilmById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Создание фильма");
        return filmService.save(film);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Обновление фильма");
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer idUser) {
        log.info("Ставим лайк фильму");
        filmService.addLike(id, idUser);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer idUser) {
        log.info("Убираем лайк фильму");
        filmService.deleteLike(id, idUser);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получение топ-10 фильмов");
        return filmService.getPopular(count);
    }
}
