package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
@Qualifier("userRowMapper")
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        try {
            LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
            if (!resultSet.wasNull()) {
                user.setBirthday(birthday);
            }
        } catch (SQLException e) {
        }
        return user;
    }
}
