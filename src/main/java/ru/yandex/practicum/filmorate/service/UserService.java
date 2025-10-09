package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.CreateFriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public List<UserDto> findAll() {
        return userStorage.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public UserDto findUser(Long id) {
        User user = userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь id = " + id + ", не найден."));
        UserDto userResponse = UserMapper.mapToUserDto(user);
        log.info("{}", userResponse);
        return userResponse;
    }

    public UserDto create(NewUserRequest userRequest) {
        User user = UserMapper.mapToUser(userRequest);
        return UserMapper.mapToUserDto(userStorage.save(user));
    }

    public UserDto update(UpdateUserRequest userRequest) {
        User user = userStorage.findById(userRequest.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь id = " + userRequest.getId() + ", не найден."));
        UserMapper.updateUserFields(user, userRequest);
        return UserMapper.mapToUserDto(userStorage.update(user));
    }

    public void addFriend(Long senderId, Long receiverId) {
        if (friendshipCreateValidation(senderId, receiverId)) {
            friendshipStorage.insertFriendship(senderId, receiverId);
        }
    }

    public List<UserDto> getFriends(Long userId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь id = " + userId + ", не найден."));
        return friendshipStorage.getFriends(userId).stream().map(id -> new UserDto(id)).toList();
    }

    public List<UserDto> getCommonFriends(Long firestUserId, Long secondUserId) {
        User user = userStorage.findById(firestUserId).orElseThrow(() -> new NotFoundException("Пользователь id = " + firestUserId + ", не найден."));
        User other = userStorage.findById(secondUserId).orElseThrow(() -> new NotFoundException("Пользователь id = " + secondUserId + ", не найден."));

        return userStorage.getCommonFriends(firestUserId, secondUserId)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public void deleteFriend(Long senderId, Long receiverId) {
        userStorage.findById(senderId).orElseThrow(() -> new NotFoundException("Пользователь id = " + senderId + ", не найден."));
        userStorage.findById(receiverId).orElseThrow(() -> new NotFoundException("Пользователь id = " + receiverId + ", не найден."));

        friendshipStorage.removeFriendship(senderId, receiverId);
    }

    private void setDefaultNameIfBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("У пользователя id={} установлено имя по умолчанию: {}", user.getId(), user.getName());
        }
    }

    private boolean friendshipCreateValidation(long senderId, long receiverId) {
        if (senderId == receiverId) {
            throw new CreateFriendsException("Нельзя добавить в друзья самого себя");
        }

        User user = userStorage.findById(senderId).orElseThrow(() -> new NotFoundException("Пользователь не id = " + senderId + " найден"));
        User friend = userStorage.findById(receiverId).orElseThrow(() -> new NotFoundException("Пользователь не id = " + receiverId + " найден"));

        if (user.getFriends().contains(receiverId)) {
            throw new CreateFriendsException("Дружба уже существует");
        }

        return true;
    }
}