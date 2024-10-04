package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;

    private String name;

    @NotEmpty
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    @NotBlank
    private String login;

    @PastOrPresent
    private LocalDate birthday;

    private final Set<Integer> friends = new HashSet<>();
}
