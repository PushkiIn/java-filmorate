package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.BaseRepository;


import java.util.List;

@Repository
public class FriendshipStorage extends BaseRepository<Friendship> {
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendships WHERE sender_id = ? AND receiver_id = ?";
    private static final String GET_COMMON_FRIENDS_QUERY = "SELECT * FROM friendships WHERE sender_id";

    public FriendshipStorage(JdbcTemplate jdbc, @Qualifier("FriendshipRowMapper") RowMapper<Friendship> mapper) {
        super(jdbc, mapper) ;
    }

    public List<Long> getFriends(long id) {
        String sql =
                "(SELECT receiver_id AS friend_id " +
                        "FROM friendships " +
                        "WHERE sender_id = ?) " +
                        "UNION " +
                        "(SELECT sender_id AS friend_id " +
                        "FROM friendships " +
                        "WHERE receiver_id = ? and confirmed = true)";
        return jdbc.query(sql, (rs, rowNum) -> rs.getLong("friend_id"), id, id);
    }

    public void removeFriendship(Long sender_id, Long receiver_id) {
        jdbc.update(DELETE_FRIENDSHIP_QUERY, sender_id, receiver_id);
    }

//    public List<User> getCommonFriends () {
//
//    }
}
