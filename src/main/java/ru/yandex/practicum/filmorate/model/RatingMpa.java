package ru.yandex.practicum.filmorate.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RatingMpa {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private final Integer id;
    private final String displayName;

    public static RatingMpa fromId(int id) {
        switch (id) {
            case 1:
                return G;
            case 2:
                return PG;
            case 3:
                return PG_13;
            case 4:
                return R;
            case 5:
                return NC_17;
            default:
                throw new NotFoundException(String.format("RatingMpa id = %d, не существует", id));
        }
    }
}
