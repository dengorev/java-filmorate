package ru.yandex.practicum.filmorate.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import java.time.LocalDate;

public class ReleaseDateValidated implements ConstraintValidator<ReleaseDate, LocalDate> {
    private LocalDate releaseDate;

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate == null || !localDate.isBefore(releaseDate);
    }

    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
        releaseDate = LocalDate.parse(constraintAnnotation.value());
    }
}
