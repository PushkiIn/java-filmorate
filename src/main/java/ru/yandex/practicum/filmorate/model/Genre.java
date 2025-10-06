package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Genre {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик");

    private final Integer id;
    private final String displayName;

    public static Genre fromId(int id) {
        switch (id) {
            case 1:
                return COMEDY;
            case 2:
                return DRAMA;
            case 3:
                return CARTOON;
            case 4:
                return THRILLER;
            case 5:
                return DOCUMENTARY;
            case 6:
                return ACTION;
            default:
                throw new NotFoundException(String.format("Genre id = %d, не существует", id));
        }
    }
}
