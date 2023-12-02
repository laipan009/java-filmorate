package ru.yandex.practicum.filmorate.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private static int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    protected boolean isUsersAlreadyFriends(int userId, int idFriend) {
        return Optional.of(users.get(userId))
                .orElseThrow(() -> new NotExistObjectException("User not exist"))
                .getFriends()
                .stream()
                .map(list -> list.getFriends().contains(idFriend))
                .findFirst()
                .orElse(false);
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Getting all users");
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        log.info("Adding user with id {}", user.getId());
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        users.put(id++, user);
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        log.info("Getting user with id {}", id);
        if (!isUserExists(id)) {
            throw new NotExistObjectException("User not exist");
        }
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user with id {}", id);
        if (!isUserExists(user.getId())) {
            throw new NotExistObjectException("User not exist");
        }
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUserById(int id) {
        log.info("Deleting user with id {}", id);
        if (!isUserExists(id)) {
            throw new NotExistObjectException("User not exist");
        }
        users.remove(id);
    }

    @Override
    public List<User> getFriendsByUserId(int id) {
        return getUserById(id)
                .orElseThrow(() -> new NotExistObjectException("User noy exist"))
                .getFriends();
    }

    @Override
    public void deleteFriendById(int userId, int idFriend) {
        List<User> userFriends = getUserById(userId)
                .orElseThrow(() -> new NotExistObjectException("User not exist"))
                .getFriends();
        List<User> friendFriends = getUserById(idFriend)
                .orElseThrow(() -> new NotExistObjectException("User not exist"))
                .getFriends();
        if (isUsersAlreadyFriends(userId, idFriend)) {
            throw new RuntimeException("Users already is friend list");
        }
        userFriends.remove(getUserById(idFriend).get());
        friendFriends.remove(getUserById(userId).get());
    }

    @Override
    public void addFriend(int userId, int idFriend) {
        List<User> userFriends = getUserById(userId)
                .orElseThrow(() -> new NotExistObjectException("User now exist"))
                .getFriends();
        List<User> friendFriends = getUserById(idFriend)
                .orElseThrow(() -> new NotExistObjectException("User now exist"))
                .getFriends();
        if (isUsersAlreadyFriends(userId, idFriend)) {
            throw new RuntimeException("Users already is friend list");
        }
        userFriends.add(getUserById(idFriend).get());
        friendFriends.add(getUserById(userId).get());
    }

    @Override
    public List<User> getCommonFriends(int userId, int idFriend) {
        List<User> userFriends = getUserById(userId)
                .orElseThrow(() -> new NotExistObjectException("User now exist"))
                .getFriends();
        List<User> friendFriends = getUserById(idFriend)
                .orElseThrow(() -> new NotExistObjectException("User now exist"))
                .getFriends();
        Set<User> commonFriends = Stream.of(friendFriends, userFriends)
                .flatMap(Collection::stream)
                .filter(u -> u.getId() != userId && u.getId() != idFriend)
                .collect(Collectors.toSet());
        return new ArrayList<>(commonFriends);
    }

    public boolean isUserExists(int id) {
        log.info("Checking user with id {} existence", id);
        return users.containsKey(id);
    }
}
