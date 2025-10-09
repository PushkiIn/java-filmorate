package ru.yandex.practicum.filmorate.storage.DbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;


@Slf4j
@Primary
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = """
            SELECT
                f.film_id,
                f.name,
                f.description,
                f.duration,
                f.release_date,
                f.rating_id,
                r.name AS rating_name,
                STRING_AGG(g.genre_id::text, ',') AS genre_ids,
                STRING_AGG(g.name, ',') AS genre_names
            FROM films AS f
            LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id
            LEFT JOIN genres AS g ON fg.genre_id = g.genre_id
            LEFT JOIN ratings AS r ON f.rating_id = r.rating_id
            GROUP BY f.film_id
            ORDER BY f.film_id
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT
                f.film_id,
                f.name,
                f.description,
                f.duration,
                f.release_date,
                f.rating_id,
                r.name AS rating_name,
                STRING_AGG(g.genre_id::text, ',') AS genre_ids,
                STRING_AGG(g.name, ',') AS genre_names
            FROM films AS f
            LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id
            LEFT JOIN genres AS g ON fg.genre_id = g.genre_id
            LEFT JOIN ratings AS r ON f.rating_id = r.rating_id
            WHERE f.film_id = ?
            GROUP BY f.film_id
            """;

    private static final String INSERT_QUERY = """
            INSERT INTO films(name, description, release_date, duration, rating_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String INSERT_GENRES_QUERY = """
            INSERT INTO films_genres (film_id, genre_id)
            VALUES (?, ?)
            """;

    private static final String FIND_GENRES_QUERY = """
            SELECT genre_id
            FROM films_genres WHERE film_id = ?
            """;

    private static final String UPDATE_FILMS_QUERY = """
            UPDATE films
            SET
               name = ?,
               description = ?,
               release_date = ?,
               duration = ?,
               rating_id = ?
            WHERE film_id = ?
            """;

    private static final String INSERT_LIKE_QUERY = """
            INSERT INTO films_likes (film_id, user_id)
            VALUES (? , ?)
            """;

    private static final String DELETE_LIKE_QUERY = """
            DELETE FROM films_likes
            WHERE film_id = ? AND user_id = ?
            """;

    private static final String DELETE_GENRES_QUERY = """
            DELETE FROM films_genres
            WHERE film_id = ?
            """;
    private static final String FIND_POPULAR_FILMS_QUERY = """
            SELECT
                f.film_id,
                f.name,
                f.description,
                f.duration,
                f.release_date,
                f.rating_id,
                r.name AS rating_name,
                STRING_AGG(g.genre_id::text, ',') AS genre_ids,
                STRING_AGG(g.name, ',') AS genre_names,
                COUNT(fl.user_id) AS likes_count
            FROM public.films AS f
            LEFT JOIN public.films_genres fg ON f.film_id = fg.film_id
            LEFT JOIN public.genres g ON fg.genre_id = g.genre_id
            JOIN public.ratings AS r ON f.rating_id = r.rating_id
            LEFT JOIN public.films_likes AS fl ON f.film_id = fl.film_id
            GROUP BY f.film_id, f.name, f.description, f.duration, f.release_date, f.rating_id, r.name
            ORDER BY likes_count DESC
            LIMIT ?
            """;

    public FilmDbStorage(JdbcTemplate jdbc, @Qualifier("FilmRowMapper") RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film save(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRatingMpa().getId());
        film.setId(id);
        if (!(film.getGenres() == null || film.getGenres().isEmpty())) {
            for (Genre genre : film.getGenres()) {
                jdbc.update(INSERT_GENRES_QUERY, film.getId(), genre.getId());
            }
        }
        log.debug("Сохранили фильм в БД {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_FILMS_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRatingMpa().getId(),
                film.getId());

        update(DELETE_GENRES_QUERY, film.getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                update(INSERT_GENRES_QUERY, film.getId(), genre.getId());
            }
        }

        return film;
    }

    @Override
    public Optional<Film> findById(Long id) {
        Optional<Film> filmOp = findOne(FIND_BY_ID_QUERY, id);
        return filmOp;
    }

    @Override
    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<Film> findPopularFilm(int count) {
        return findMany(FIND_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        update(INSERT_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        update(DELETE_LIKE_QUERY, filmId, userId);
    }
}
