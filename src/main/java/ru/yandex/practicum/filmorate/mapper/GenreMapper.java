package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreMapper {
    public static GenreDto mapToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getDisplayName());
    }

    public static Genre mapToMpa(GenreDto genreDto) {
        return Genre.fromId(genreDto.getId());
    }
}
