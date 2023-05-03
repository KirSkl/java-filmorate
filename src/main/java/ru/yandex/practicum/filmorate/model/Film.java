package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
}
