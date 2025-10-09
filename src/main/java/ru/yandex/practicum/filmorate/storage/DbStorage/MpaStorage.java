package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaStorage extends BaseRepository<RatingMpa> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ratings";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ratings WHERE rating_id = ?";

    public MpaStorage(JdbcTemplate jdbc, RowMapper<RatingMpa> rowMapper) {
        super(jdbc, rowMapper);
    }

    public Optional<RatingMpa> findById(Long mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }

    public List<RatingMpa> findAll() {
        return findMany(FIND_ALL_QUERY);
    }
}
