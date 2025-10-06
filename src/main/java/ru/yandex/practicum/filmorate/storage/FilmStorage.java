package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends BaseStorage<Film> {
    List<Integer> findGenresByFilmId(Long filmId);

    List<Film> findPopularFilm(int count);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}
