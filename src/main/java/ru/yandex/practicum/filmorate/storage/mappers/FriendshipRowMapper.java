package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component("FriendshipRowMapper")
public class FriendshipRowMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setSenderId(resultSet.getLong("sender_id"));
        friendship.setReceiverId(resultSet.getLong("receiver_id"));
        friendship.setConfirmed(resultSet.getBoolean("confirmed"));
        return friendship;
    }
}
