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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public List<UserDto> findAll() {
        List<UserDto> response = userStorage.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        response.stream().forEach(ur -> ur.setFriends(Set.copyOf(friendshipStorage.getFriends(ur.getId()))));
        return response;
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
            userStorage.insertFriendship(senderId, receiverId);
        }
    }

    public List<UserDto> getFriends(Long userId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь id = " + userId + ", не найден."));
        return friendshipStorage.getFriends(userId).stream().map(id -> new UserDto(id)).toList();
    }

    public List<UserDto> getCommonFriends(Long userId, Long otherId) {
        User user = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь id = " + userId + ", не найден."));
        ;
        User other = userStorage.findById(otherId).orElseThrow(() -> new NotFoundException("Пользователь id = " + userId + ", не найден."));
        ;

        List<Long> friends = friendshipStorage.getFriends(userId);
        List<Long> friendsOther = friendshipStorage.getFriends(otherId);

        Set<Long> commonFriends = new HashSet<>();
        commonFriends.addAll(friends);
        commonFriends.addAll(friendsOther);

        return commonFriends.stream().filter(id -> friends.contains(id)).filter(id -> friendsOther.contains(id)).map(id -> UserMapper.mapToUserDto(userStorage.findById(id).get())).toList();
    }

    public void deleteFriend(Long senderId, Long receiverId) {
        userStorage.findById(senderId).orElseThrow(() -> new NotFoundException("Пользователь id = " + senderId + ", не найден."));
        ;
        userStorage.findById(receiverId).orElseThrow(() -> new NotFoundException("Пользователь id = " + receiverId + ", не найден."));
        ;

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

        if (user.getFriends().containsKey(receiverId)) {
            throw new CreateFriendsException("Дружба уже существует");
        }

        return true;
    }
}