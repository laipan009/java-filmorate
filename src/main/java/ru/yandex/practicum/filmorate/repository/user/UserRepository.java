package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAllUsers();

    User addUser(User user);

    Optional<User> getUserById(int id);

    User updateUser(User user);

    void deleteUserById(int id);

    List<User> getFriendsByUserId(int id);

    void deleteFriendById(int userId, int idFriend);

    void addFriend(int userId, int idFriend);

    List<User> getCommonFriends(int userId, int idFriend);
}