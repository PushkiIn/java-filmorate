package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmMapper {
    public static Film mapToFilm(NewFilmRequest request) {
        Film film = new Film();
        film.setName(request.getName());
        film.setDescription(request.getDescription());
        film.setDuration(request.getDuration());
        film.setReleaseDate(request.getReleaseDate());
        film.setRatingMpa(new RatingMpa(request.getMpa().getId(), request.getMpa().getName()));
        if (!(request.getGenres() == null || request.getGenres().isEmpty())) {
            film.setGenres(request.getGenres().stream()
                    .map(genreDto -> new Genre(genreDto.getId(), genreDto.getName()))
                    .collect(Collectors.toSet()));
        }
        return film;
    }

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setDuration(film.getDuration());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setMpa(new MpaDto(film.getRatingMpa().getId(), film.getRatingMpa().getName()));
        dto.setGenres(film.getGenres().stream()
                .map(genre -> new GenreDto(genre.getId(), genre.getName()))
                .sorted(Comparator.comparing(GenreDto::getId))
                .toList());
        dto.setLikesCount(film.getLikesCount());
        return dto;
    }

    public static Film updateFilmFields(Film film, UpdateFilmRequest request) {
        if (request.hasName()) {
            film.setName(request.getName());
        }
        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }
        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }
        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }
        if (request.hasMpa()) {
            film.setRatingMpa(new RatingMpa(request.getMpa().getId(), request.getName()));
        }
        if (request.hasGenres()) {
            film.setGenres(request.getGenres().stream()
                    .map(genreDto -> new Genre(genreDto.getId(), genreDto.getName()))
                    .collect(Collectors.toSet()));
        }
        log.debug("После обновления полей {}", film);
        return film;
    }
}
