package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film save(Film film);

    List<Film> getAll();

    Film update(Film film);

    Film getFilmById(Integer id);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    List<Film> getPopular(Integer count);
}
