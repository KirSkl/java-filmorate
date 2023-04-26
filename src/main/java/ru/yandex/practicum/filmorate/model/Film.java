package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private final int id;
    @NotBlank
    private final String name;
    @Size(max=200, message = "максимальная длина описания — 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    private final Duration duration;
}
