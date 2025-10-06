package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<FilmDto> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDto findById(@PathVariable Long filmId) {
        return filmService.findById(filmId);
    }

    @PostMapping
    public FilmDto create(@Valid @RequestBody NewFilmRequest request) {
        log.debug("сохраняем фильм");
        return filmService.createFilm(request);
    }


    @PutMapping
    public FilmDto update(@Valid @RequestBody UpdateFilmRequest request) {
        log.debug("обновляем фильм");
        return filmService.updateFilm(request);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public FilmDto addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public FilmDto removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> findPopularFilm(@RequestParam(defaultValue = "10") int count) {
        return filmService.findPopularFilm(count);
    }
}