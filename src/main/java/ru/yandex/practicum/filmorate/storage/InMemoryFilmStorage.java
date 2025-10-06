package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film save(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public Film update(Film entity) {
        return null;
    }

    @Override
    public Optional<Film> findById(Long id) {
        if (!films.containsKey(id)) {
            log.error("Попытка получить несуществующий фильм с id {}", id);
            throw new NotFoundException(String.format("Фильм с id %s не найден!", id));
        }
        return Optional.of(films.get(id));
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void deleteById(Long id) {
        films.remove(id);
    }

    @Override
    public List<Integer> findGenresByFilmId(Long filmId) {
        return List.of();
    }

    @Override
    public List<Film> findPopularFilm(int count) {
        return List.of();
    }

    @Override
    public void addLike(Long filmId, Long userId) {

    }

    @Override
    public void removeLike(Long filmId, Long userId) {

    }
}
