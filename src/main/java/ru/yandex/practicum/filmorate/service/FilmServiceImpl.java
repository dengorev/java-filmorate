package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private int generatedId = 1;

    @Override
    public Film save(Film film) {
        if (film != null) {
            log.info("Создание фильма {}", film.getName());
            film.setId(generatedId);
            getNextId();
        }
        return filmStorage.save(film);
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film update(Film newFilm) {
        Film oldFilm = filmStorage.getFilmById(newFilm.getId());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setName(newFilm.getName());
        return filmStorage.update(oldFilm);
    }

    @Override
    public Film addLike(int id, int userId) {
        userService.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getLikes().add(userId);
        film.setLikeCount(film.getLikeCount() + 1);
        return film;
    }

    @Override
    public Film deleteLike(int id, int userId) {
        userService.getUserById(userId);
        Film film = filmStorage.getFilmById(id);
        film.getLikes().remove(userId);
        film.setLikeCount(film.getLikeCount() - 1);
        return film;
    }

    @Override
    public Collection<Film> getPopular(int count) {
        Comparator<Film> comparator = Comparator.comparingInt(x -> x.getLikes().size());
        return filmStorage.getAll().stream()
                .sorted(comparator.reversed())
                .limit(count)
                .toList();
    }

    @Override
    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    private int getNextId() {
        return generatedId++;
    }
}
