package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreStorage extends BaseRepository<Genre> implements BaseStorage<Genre> {
    private final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";

    public GenreStorage(JdbcTemplate jdbc, RowMapper<Genre> rowMapper) {
        super(jdbc, rowMapper);
    }

    @Override
    public Genre save(Genre entity) {
        return null;
    }

    @Override
    public Genre update(Genre entity) {
        return null;
    }

    @Override
    public Optional<Genre> findById(Long genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    @Override
    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public void deleteById(Long id) {

    }
}
