package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    protected final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userDbRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User addUser(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userRepository.addUser(user);
    }

    public User getUserById(int id) {
        try {
            return userRepository.getUserById(id)
                    .orElseThrow(() -> new NotExistObjectException("User with id " + id + " does not exist."));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistObjectException("User with id " + id + " does not exist.");
        }
    }

    public User updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public void deleteUserById(int id) {
        userRepository.deleteUserById(id);
    }

    public void addFriend(int userId, int idFriend) {
        userRepository.addFriend(userId, idFriend);
    }

    public void deleteFriendById(int userId, int idFriend) {
        userRepository.deleteFriendById(userId, idFriend);
    }

    public List<User> getFriendsByIdUser(int id) {
        return userRepository.getFriendsByUserId(id);
    }

    public List<User> getCommonFriends(int userId, int idFriend) {
        return new ArrayList<>(userRepository.getCommonFriends(userId, idFriend));
    }
}
