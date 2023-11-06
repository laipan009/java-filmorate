package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserService {
    protected final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected boolean isUsersAlreadyFriends(int userId, int idFriend) {
        Optional<Set<Integer>> friends = Optional.ofNullable(userRepository.getUserById(userId).getFriends());
        return friends.map(set -> set.contains(idFriend)).orElse(false);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public void deleteUserById(int id) {
        userRepository.deleteUserById(id);
    }

    public void addFriend(int userId, int idFriend) {
        if (!userRepository.isUserExists(userId) || !userRepository.isUserExists(idFriend)) {
            throw new NotExistObjectException("Users not exist");
        }
        if (isUsersAlreadyFriends(userId, idFriend)) {
            throw new RuntimeException("Users already is friend list");
        }
        Set<Integer> userFriends = userRepository.getUserById(userId).getFriends();
        userFriends.add(idFriend);
        Set<Integer> friendsNewFriend = userRepository.getUserById(idFriend).getFriends();
        friendsNewFriend.add(userId);
    }

    public void deleteFriend(int userId, int idFriend) {
        if (!userRepository.isUserExists(userId) || !userRepository.isUserExists(idFriend)) {
            throw new NotExistObjectException("Users not exist");
        }
        if (!isUsersAlreadyFriends(userId, idFriend)) {
            throw new RuntimeException("Users are not on the friends list");
        }
        Set<Integer> userFriends = userRepository.getUserById(userId).getFriends();
        userFriends.remove(idFriend);
        Set<Integer> friendsNewFriend = userRepository.getUserById(idFriend).getFriends();
        friendsNewFriend.remove(userId);
    }

    public List<User> getFriendsByIdUser(int id) {
        if (!userRepository.isUserExists(id)) {
            throw new NotExistObjectException("User not exist");
        }
        Optional<Set<Integer>> friends = Optional.of(userRepository.getUserById(id).getFriends());
        return friends.orElseThrow(() -> new RuntimeException("User not have friends"))
                .stream()
                .map(userRepository::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int idFriend) {
        if (!userRepository.isUserExists(userId) || !userRepository.isUserExists(idFriend)) {
            throw new NotExistObjectException("Users not exist");
        }
        Set<Integer> userFriends = userRepository.getUserById(userId).getFriends();
        Set<Integer> friendFriends = userRepository.getUserById(idFriend).getFriends();
        Set<User> commonFriends = Stream.of(friendFriends, userFriends)
                .flatMap(Collection::stream)
                .filter(id -> id != userId && id != idFriend)
                .map(userRepository::getUserById)
                .collect(Collectors.toSet());
        return new ArrayList<>(commonFriends);
    }
}
