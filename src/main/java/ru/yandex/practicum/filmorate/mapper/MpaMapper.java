package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.RatingMpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static MpaDto mapToDto(RatingMpa ratingMpa) {
        return new MpaDto(ratingMpa.getId(), ratingMpa.getDisplayName());
    }

    public static RatingMpa mapToMpa(MpaDto mpaDto) {
        return RatingMpa.fromId(mpaDto.getId());
    }
}
