package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.NotBefore;
import ru.yandex.practicum.filmorate.validation.groups.OnCreate;

import java.time.LocalDate;
import java.util.List;

@Data
public class NewFilmRequest {
    @NotBlank(groups = OnCreate.class)
    private String name;

    @Size(max = 200, groups = OnCreate.class)
    private String description;

    @NotBefore(minData = "1895-12-28", groups = OnCreate.class)
    private LocalDate releaseDate;

    @Positive(groups = OnCreate.class)
    private Integer duration;

    private List<GenreDto> genres;

    private MpaDto mpa;
}