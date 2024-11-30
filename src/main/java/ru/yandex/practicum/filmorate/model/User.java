package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private Integer id;

    @NotBlank
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    private String name;

    @NotBlank
    @Pattern(regexp = "^\\S+$")
    private String login;

    @PastOrPresent
    private LocalDate birthday;

    @JsonIgnore
    private Set<Integer> friends;
}
