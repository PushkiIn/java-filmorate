package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmService {
    FilmStorage filmStorage;
    UserService userService;
    GenreService genreService;
    MpaService mpaService;

    public List<FilmDto> getFilms() {
        return filmStorage.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto findById(Long filmId) {
        FilmDto filmDto = filmStorage.findById(filmId)
                .map(FilmMapper::mapToFilmDto)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id = %d, не найден", filmId)));
        return filmDto;
    }

    public FilmDto createFilm(NewFilmRequest filmRequest) {
        Film film = FilmMapper.mapToFilm(filmRequest);
        validate(film);
        return FilmMapper.mapToFilmDto(filmStorage.save(film));
    }

    public FilmDto updateFilm(UpdateFilmRequest filmRequest) {
        Film film = filmStorage.findById(filmRequest.getId()).orElseThrow(() -> new NotFoundException(String.format("Фильм с id = %d, не найден", filmRequest.getId())));
        FilmMapper.updateFilmFields(film, filmRequest);
        validate(film);
        return FilmMapper.mapToFilmDto(filmStorage.update(film));
    }

    public FilmDto addLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException(String.format("Фильм с id = %d, не найден", filmId)));
        userService.findUser(userId);
        filmStorage.addLike(filmId, userId);
        return FilmMapper.mapToFilmDto(film);
    }

    public FilmDto removeLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId).orElseThrow(() -> new NotFoundException(String.format("Фильм с id = %d, не найден", filmId)));
        userService.findUser(userId);
        filmStorage.removeLike(filmId, userId);
        return FilmMapper.mapToFilmDto(film);
    }

    public List<FilmDto> findPopularFilm(int count) {
        return filmStorage.findPopularFilm(count).stream().map(FilmMapper::mapToFilmDto).toList();
    }

    public void validate(Film film) {
        for (Genre genre : film.getGenres()) {
            genreService.findById((long) genre.getId());
        }
        mpaService.findById((long) film.getRatingMpa().getId());
    }
}
