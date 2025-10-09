package ru.yandex.practicum.filmorate.storage.DbStorage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FriendshipStorage {
    private final JdbcTemplate jdbc;
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendships WHERE sender_id = ? AND receiver_id = ?";
    private static final String GET_FRIENDSHIP_QUERY = "SELECT receiver_id FROM friendships WHERE sender_id = ?";
    private static final String INSERT_FRIENDSHIP_QUERY = "INSERT INTO friendships (sender_id, receiver_id) VALUES (?, ?)";


    public List<Long> getFriends(long id) {
        log.debug("Получение друзей:");
        List<Long> friends = jdbc.query(GET_FRIENDSHIP_QUERY, (rs, rowNum) -> rs.getLong("receiver_id"), id);
        log.debug("Список друзей:", friends);
        return friends;
    }

    public void removeFriendship(Long senderId, Long receiverId) {
        jdbc.update(DELETE_FRIENDSHIP_QUERY, senderId, receiverId);
    }

    public void insertFriendship(Long senderId, Long receiverId) {
        jdbc.update(INSERT_FRIENDSHIP_QUERY, senderId, receiverId);
    }
}
