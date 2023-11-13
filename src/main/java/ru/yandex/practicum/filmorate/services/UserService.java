package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    protected final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected boolean isUsersAlreadyFriends(int userId, int idFriend) {
        return Optional.ofNullable(userRepository.getUserById(userId).getFriends())
                .map(set -> set.contains(idFriend))
                .orElse(false);
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
        Set<Integer> userFriends = userRepository.getUserById(userId).getFriends();
        Set<Integer> friendsNewFriend = userRepository.getUserById(idFriend).getFriends();
        if (isUsersAlreadyFriends(userId, idFriend)) {
            throw new RuntimeException("Users already is friend list");
        }
        userFriends.add(idFriend);
        friendsNewFriend.add(userId);
    }

    public void deleteFriend(int userId, int idFriend) {
        Set<Integer> userFriends = userRepository.getUserById(userId).getFriends();
        Set<Integer> friendsNewFriend = userRepository.getUserById(idFriend).getFriends();
        if (!isUsersAlreadyFriends(userId, idFriend)) {
            throw new RuntimeException("Users are not on the friends list");
        }
        userFriends.remove(idFriend);
        friendsNewFriend.remove(userId);
    }

    public List<User> getFriendsByIdUser(int id) {
        Optional<Set<Integer>> friends = Optional.of(userRepository.getUserById(id).getFriends());
        return friends.orElseThrow(() -> new RuntimeException("User not have friends"))
                .stream()
                .map(userRepository::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int idFriend) {

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
