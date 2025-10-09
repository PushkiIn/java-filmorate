package ru.yandex.practicum.filmorate.storage.DbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Primary
@Repository
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users ORDER BY user_id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = """
            UPDATE users
            SET email = ?, login = ?, name = ?, birthday = ?
            WHERE user_id = ?
            """;
    private static final String GET_COMMON_FRIENDS_QUERY = """
            SELECT *
            FROM users
            WHERE user_id IN (
                SELECT receiver_id
                FROM friendships
                WHERE sender_id = ? AND receiver_id IN (
                    SELECT receiver_id
                    FROM friendships
                    WHERE sender_id = ?
                )
            )
            ORDER BY user_id
            """;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbc, @Qualifier("userRowMapper") RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User save(User user) {
        log.info("Слой репозитория сохранение пользователя {}", user);
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        return findMany(GET_COMMON_FRIENDS_QUERY, firstUserId, secondUserId);
    }
}
