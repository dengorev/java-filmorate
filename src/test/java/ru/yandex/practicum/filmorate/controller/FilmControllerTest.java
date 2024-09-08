package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    public void whenFilmNameIsEmptyThenThrowValidationException() {
        Film film = Film.builder()
                .name("")
                .description("описание")
                .releaseDate(LocalDate.of(2020, 5, 25))
                .duration(90)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
    }

    @Test
    public void whenFilmSizeDescriptionIsMore200ThenThrowValidationException() {
        Film film = Film.builder()
                .name("название")
                .description("описаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописан" +
                        "описаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеопи" +
                        "описаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописаниеописа")
                .releaseDate(LocalDate.of(2020, 5, 25))
                .duration(90)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
    }

    @Test
    public void whenFilmDateReleaseAfter1895YearThenThrowValidationException() {
        Film film = Film.builder()
                .name("название")
                .description("описание")
                .releaseDate(LocalDate.of(1893, 5, 25))
                .duration(90)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
    }

    @Test
    public void whenFilmDurationIsPositiveThenThrowValidationException() {
        Film film = Film.builder()
                .name("название")
                .description("описание")
                .releaseDate(LocalDate.of(2020, 5, 25))
                .duration(-3)
                .build();

        Assertions.assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
    }
}
