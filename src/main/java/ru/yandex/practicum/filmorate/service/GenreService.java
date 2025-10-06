package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.DbStorage.GenreStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreDto findById(Long genreId) {
        return GenreMapper.mapToDto(genreStorage.findById(genreId).orElseThrow(() -> new NotFoundException("Жанр id = " + genreId + ", не найден.")));
    }

    public List<GenreDto> findAll() {
        return genreStorage.findAll().stream().map(GenreMapper::mapToDto).sorted(Comparator.comparing(GenreDto::getId)).toList();
    }
}
