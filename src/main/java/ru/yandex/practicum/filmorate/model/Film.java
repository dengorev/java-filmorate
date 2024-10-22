package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    private Integer id;

    @NotBlank(message = "Имя фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длинна описания 200 символов")
    private String description;

    @ReleaseDate
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @NotNull
    private Mpa mpa;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Genre> genres;
}
