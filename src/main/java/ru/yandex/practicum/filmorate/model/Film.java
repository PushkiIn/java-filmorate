package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

import java.time.LocalDate;

@Data
public class Film {
    @NotNull(message = "id обязательно", groups = OnUpdate.class)
    private Long id;

    @NotBlank(message = "Название обязательно", groups = OnCreate.class)
    private String name;

    @Size(message = "Описание должно быть не длиннее {max} символов", groups = {OnCreate.class, OnUpdate.class}, max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Длительность не может быть отрицательной", groups = {OnCreate.class, OnUpdate.class})
    private Long duration;

    private boolean releaseDateValid = isReleaseDateValid();

    @AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года", groups = {OnCreate.class, OnUpdate.class})
    private boolean isReleaseDateValid() {
        if(releaseDate == null) {
            return true;
        }
        return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}