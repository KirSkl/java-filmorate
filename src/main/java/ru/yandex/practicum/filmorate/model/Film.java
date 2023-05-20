package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank
    private final String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Set<Long> likes;

    @JsonCreator
    public Film(@JsonProperty Long id, @JsonProperty String name, @JsonProperty String description,
            @JsonProperty LocalDate releaseDate, @JsonProperty int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
    }

    @JsonCreator
    public Film(@JsonProperty String name, @JsonProperty String description, @JsonProperty LocalDate releaseDate,
            @JsonProperty int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
