package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Long likesCount;
    private Set<Genre> genres = new HashSet<>();
    private RatingMpa ratingMpa;

    public void setGenresFromListIds(List<Integer> genresIds) {
        for (Integer id : genresIds) {
            genres.add(Genre.fromId(id));
        }
    }
}