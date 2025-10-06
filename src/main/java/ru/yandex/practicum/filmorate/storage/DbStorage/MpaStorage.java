package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaStorage extends BaseRepository<RatingMpa> implements BaseStorage<RatingMpa> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE rating_id = ?";

    public MpaStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> rowMapper) {
        super(jdbc, rowMapper);
    }

    @Override
    public RatingMpa save(RatingMpa entity) {
        return null;
    }

    @Override
    public RatingMpa update(RatingMpa entity) {
        return null;
    }

    @Override
    public Optional<RatingMpa> findById(Long mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }

    @Override
    public List<RatingMpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public void deleteById(Long id) {

    }
}
