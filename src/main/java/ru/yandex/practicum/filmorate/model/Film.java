package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @NotBlank
    private final String name;
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Long id;
    private Set<Long> likes;
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @JsonProperty("mpa")
    private MPARating mpaRating;
    private Set<Genre> genres;


    public Film(Long id, String name, String description,
            LocalDate releaseDate, int duration, MPARating mpaRating, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.mpaRating = mpaRating;
        this.genres = genres;
    }

    @JsonCreator
    public Film(@JsonProperty String name, @JsonProperty String description, @JsonProperty LocalDate releaseDate,
            @JsonProperty int duration, @JsonProperty MPARating mpaRating, @JsonProperty Set<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = new HashSet<>();
        this.mpaRating = mpaRating;
        this.genres = genres;
    }

    public void addLike(Long id) {
        likes.add(id);
        //rate++;
    }

    public void deleteLike(Long id) {
        likes.remove(id);
        //rate--;
    }
}
