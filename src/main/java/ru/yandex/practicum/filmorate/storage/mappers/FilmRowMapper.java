package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
        film.setRatingMpa(new RatingMpa(resultSet.getInt("rating_id"), resultSet.getString("rating_name")));
        try {
            long likes = resultSet.getLong("likes_count");
            if (!resultSet.wasNull()) {
                film.setLikesCount(likes);
            }
        } catch (SQLException e) {
            log.debug(e.getMessage());
        }

        String genreIdsStr = resultSet.getString("genre_ids");
        String genreNamesStr = resultSet.getString("genre_names");

        if (genreIdsStr != null && genreNamesStr != null) {
            String[] ids = genreIdsStr.split(",");
            String[] names = genreNamesStr.split(",");

            Set<Genre> genres = new HashSet<>();

            for (int i = 0; i < ids.length; i++) {
                try {
                    long id = Long.parseLong(ids[i].trim());
                    String name = names[i].trim();
                    genres.add(new Genre((int) id, name));
                } catch (Exception e) {
                    log.debug("Ошибка при парсинге жанра: ids='{}', names='{}'", genreIdsStr, genreNamesStr, e);
                }
            }

            film.setGenres(genres);
        }
        log.debug("{}", film);
        return film;
    }
}
