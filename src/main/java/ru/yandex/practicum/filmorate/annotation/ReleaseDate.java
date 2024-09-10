package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Past;
import ru.yandex.practicum.filmorate.util.ReleaseDateValidated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidated.class)
@Past
public @interface ReleaseDate {
    String message() default "Фильм должен быть позже {value}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String value() default "1895-12-28";
}
