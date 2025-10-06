package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component("FilmRowMapper")
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));
        film.setReleaseDate(resultSet.getDate("release_date").toLocalDate());
        film.setRatingMpa(RatingMpa.fromId(resultSet.getInt("rating_id")));
        try {
            long likes = resultSet.getLong("likes_count");
            if (!resultSet.wasNull()) {
                film.setLikesCount(likes);
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }
        log.debug("{}", film);
        return film;
    }
}
