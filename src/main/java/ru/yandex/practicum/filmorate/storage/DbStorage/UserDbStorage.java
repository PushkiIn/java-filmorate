package ru.yandex.practicum.filmorate.storage.DbStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET login = ?, email = ?, name = ?, birthday = ?" +
            "WHERE user_id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String FIND_FRIENDSHIP_QUERY = "SELECT * FROM friendships WHERE sender_id = ? AND receiver_id = ? AND  CONFIRMED = ?";
    private static final String UPDATE_FRIENDSHIP_QUERY = "UPDATE friendships SET sender_id = ?, receiver_id = ?, confirmed = ? " +
            "WHERE USER_ID1 = ? AND USER_ID2 = ?";
    private static final String INSERT_FRIENDSHIP_QUERY = "INSERT INTO friendships (sender_id, receiver_id, confirmed) VALUES(?, ?, ?)";

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
                user.getLogin(),
                user.getEmail(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    @Override
    public void deleteById(Long id) {
        update(DELETE_BY_ID_QUERY, id);
    }

    @Override
    public boolean containsFriendship(Long sender_id, Long receiver_id, Boolean filterConfirmed) {
        SqlRowSet rows = jdbc.queryForRowSet(FIND_FRIENDSHIP_QUERY, sender_id, receiver_id, filterConfirmed);
        return rows.next();
    }

    @Override
    public void updateFriendship(Long senderId, Long receiverId, boolean confirmed) {
        jdbc.update(UPDATE_FRIENDSHIP_QUERY, senderId, receiverId, confirmed, receiverId, senderId);
    }

    @Override
    public void insertFriendship(Long sender_id, Long receiver_id) {
        jdbc.update(INSERT_FRIENDSHIP_QUERY, sender_id, receiver_id, false);
    }
}
