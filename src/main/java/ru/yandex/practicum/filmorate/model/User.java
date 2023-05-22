package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {
    private Long id;
    @NotBlank(message = "Поле с e-mail не должно быть пустым")
    @Email(message = "Некорректный формат e-mail")
    private String email;
    @NotBlank(message = "Логин не должен быть пустым")
    private String login;
    private String name;
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate birthday;
    private Set<Long> friends;

    @JsonCreator
    public User(@JsonProperty Long id, @JsonProperty String email, @JsonProperty String login, @JsonProperty String name,
            @JsonProperty LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank()) ? login : name;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }


    public User( String email,  String login,  String name,
             LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank()) ? login : name;
        this.birthday = birthday;
    }
}
