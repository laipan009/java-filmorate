package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DAO.UserDAO;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService {
    protected final UserDAO userDAO;

    protected boolean isUserExists(int userId) {
        return userDAO.getAllUsers().stream()
                .anyMatch(user -> user.getId() == userId);
    }

    protected boolean isUsersAlreadyFriends(int userId, int idFriend) {
        Optional<Set<Integer>> friends = Optional.ofNullable(userDAO.getUserById(userId).getFriends());
        return friends.map(set -> set.contains(idFriend)).orElse(false);
    }

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void addFriend(int userId, int idFriend) {
        if (!isUserExists(userId) || !isUserExists(idFriend)) {
            throw new NotExistObjectException("Users not exist");
        }
        if (isUsersAlreadyFriends(userId, idFriend)) {
            throw new RuntimeException("Users already is friend list");
        }
        Set<Integer> userFriends = userDAO.getUserById(userId).getFriends();
        userFriends.add(idFriend);
        Set<Integer> friendsNewFriend = userDAO.getUserById(idFriend).getFriends();
        friendsNewFriend.add(userId);
    }

    public void deleteFriend(int userId, int idFriend) {
        if (!isUserExists(userId) || !isUserExists(idFriend)) {
            throw new NotExistObjectException("Users not exist");
        }
        if (!isUsersAlreadyFriends(userId, idFriend)) {
            throw new RuntimeException("Users are not on the friends list");
        }
        Set<Integer> userFriends = userDAO.getUserById(userId).getFriends();
        userFriends.remove(idFriend);
        Set<Integer> friendsNewFriend = userDAO.getUserById(idFriend).getFriends();
        friendsNewFriend.remove(userId);
    }

    public List<User> getFriendsByIdUser(int id) {
        if (!isUserExists(id)) {
            throw new NotExistObjectException("User not exist");
        }
        Optional<Set<Integer>> friends = Optional.of(userDAO.getUserById(id).getFriends());
        return friends.orElseThrow(() -> new RuntimeException("User not have friends"))
                .stream()
                .map(userDAO::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int idFriend) {
        if (!isUserExists(userId) || !isUserExists(idFriend)) {
            throw new NotExistObjectException("Users not exist");
        }
        Set<Integer> userFriends = userDAO.getUserById(userId).getFriends();
        Set<Integer> friendFriends = userDAO.getUserById(idFriend).getFriends();
        Set<User> commonFriends = Stream.of(friendFriends, userFriends)
                .flatMap(Collection::stream)
                .filter(id -> id != userId && id != idFriend)
                .map(userDAO::getUserById)
                .collect(Collectors.toSet());
        return new ArrayList<>(commonFriends);
    }
}
