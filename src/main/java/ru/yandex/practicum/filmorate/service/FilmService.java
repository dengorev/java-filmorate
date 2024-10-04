package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Film save(Film film);
    List<Film> getAll();
    Film update(Film film);
    Film getFilmById(int id);
    Film addLike(int id, int userId);
    Film deleteLike(int id, int userId);
    Collection<Film> getPopular(int count);
}

