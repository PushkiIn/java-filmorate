package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.NotBefore;
import ru.yandex.practicum.filmorate.validation.groups.OnUpdate;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateFilmRequest {
    @NotNull(message = "id обязательно", groups = OnUpdate.class)
    private Long id;

    private String name;

    @Size(groups = OnUpdate.class, max = 200)
    private String description;

    @NotBefore(groups = OnUpdate.class, minData = "1895-12-28")
    private LocalDate releaseDate;

    @Positive(groups = OnUpdate.class)
    private Integer duration;

    private List<GenreDto> genres;

    private MpaDto mpa;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean hasDuration() {
        return !(duration == null);
    }

    public boolean hasReleaseDate() {
        return !(releaseDate == null);
    }

    public boolean hasGenres() {
        return !(genres == null || genres.isEmpty());
    }

    public boolean hasMpa() {
        return !(mpa == null);
    }
}