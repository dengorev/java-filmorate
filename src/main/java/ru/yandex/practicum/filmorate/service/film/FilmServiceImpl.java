package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film save(Film film) {
        return filmStorage.save(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        filmStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }
}
