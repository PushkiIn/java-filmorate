package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Validated(OnCreate.class) @RequestBody Film film) {
        film.setId(getNextId());

        films.put(film.getId(), film);
        log.info("Создан фильм: id={}, name={}",
                film.getId(), film.getName());
        return film;
    }

    @PutMapping
    public Film update(@Validated(OnUpdate.class) @RequestBody Film newFilm) {
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
                oldFilm.setName(newFilm.getName());
                log.debug("Поле name фильма с id={} обновлено, новое значение name={}", oldFilm.getId(), oldFilm.getName());
            }

            if (newFilm.getDescription() != null && !newFilm.getDescription().isBlank()) {
                oldFilm.setDescription(newFilm.getDescription());
                log.debug("Поле description фильма с id={} обновлено, новое значение descriprion={}", oldFilm.getId(), oldFilm.getDescription());
            }

            if (newFilm.getReleaseDate() != null) {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                log.debug("Поле releaseDate фильма с id={} обновлено, новое значение releaseDate={}", oldFilm.getId(), oldFilm.getReleaseDate());
            }

            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
                log.debug("Поле duration фильма с id={} обновлено, новое значение duration={}", oldFilm.getId(), oldFilm.getDuration());
            }

            log.info("Обновлён фильм: id={}, name={}",
                    oldFilm.getId(), oldFilm.getName());
            return oldFilm;
        } else {
            throw new NotFoundException("Фильм с id " + newFilm.getId() + " не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}