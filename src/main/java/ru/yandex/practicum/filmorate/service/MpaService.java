package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.DbStorage.MpaStorage;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaDto findById(Long mpaId) {
        return MpaMapper.mapToDto(mpaStorage.findById(mpaId).orElseThrow(() -> new NotFoundException("Рейтинг id = " + mpaId + ", не найден.")));
    }

    public List<MpaDto> findAll() {
        return mpaStorage.findAll().stream().map(MpaMapper::mapToDto).sorted(Comparator.comparing(MpaDto::getId)).toList();
    }
}
