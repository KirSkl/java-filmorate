package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private static final Logger log = LoggerFactory.getLogger(MpaController.class);
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<MPARating> findAllGenres() {
        log.info("Получен запрос GET /genres - получить все жанры");
        return mpaService.getAllRatings();
    }

    @GetMapping("/{id}")
    public MPARating getGenreById(@PathVariable int id) {
        log.info("Получен запрос GET /genres/id - получение жанра по ID");
        Validator.validateID(id);
        return mpaService.getMpaById(id);
    }
}
