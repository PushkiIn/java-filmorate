package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film save(Film film);

    Film update(Film entity);

    Optional<Film> findById(Long id);

    List<Film> findAll();

    List<Film> findPopularFilm(int count);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}
