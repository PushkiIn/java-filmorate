package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        if (!users.containsKey(id)) {
            log.error("Попытка получить пользователя с id {}", id);
            throw new NotFoundException(String.format("Пользователь с id %s не найден!", id));
        }
        return Optional.of(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void insertFriendship(Long senderId, Long receiverId) {

    }

    @Override
    public void updateFriendship(Long senderId, Long receiverId, boolean confirmed) {

    }

    @Override
    public boolean containsFriendship(Long senderId, Long receiverId, Boolean filterConfirmed) {
        return false;
    }
}
