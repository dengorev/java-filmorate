package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenreById(Integer id);

    List<Genre> getAllGenres();

    List<Genre> getGenreListByFilmId(Integer filmId);

    void setFilmsGenres(Integer filmId, List<Genre> genres);
}
