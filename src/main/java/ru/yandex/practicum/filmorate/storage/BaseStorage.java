package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface BaseStorage<T> {
    public T save(T entity);

    public T update(T entity);

    public Optional<T> findById(Long id);

    public List<T> findAll();

    public void deleteById(Long id);
}
