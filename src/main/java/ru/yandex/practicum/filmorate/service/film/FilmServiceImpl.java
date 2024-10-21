package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final MpaService mpaService;
    private final GenreService genreService;

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film save(Film film) {
//        validate(film);
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }

//    private void validate(Film film) {
//// Validate MPA
//        Integer mpaId = film.getMpa().getId();
//        mpaService.getMpaById(film.getMpa().getId())
//                .orElseThrow(() -> new ValidationException("MPA с id = %d не найден"));
//
//// Validate Genres
//        List<Genre> allGenres = genreService.getAllGenres();
//        String invalidGenreIds = film.getGenres().stream()
//                .filter(Predicate.not(allGenres::contains))
//                .map(Genre::getId)
//                .map(Object::toString)
//                .collect(Collectors.joining(","));
//
//        if (!invalidGenreIds.isBlank()) {
//            throw new NotFoundException("MPA с идентификаторами %s не найдены");
//        }
//    }
}
