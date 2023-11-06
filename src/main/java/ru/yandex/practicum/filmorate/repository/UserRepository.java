package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User addUser(User user);

    User getUserById(int id);

    User updateUser(User user);

    void deleteUserById(int id);

    boolean isUserExists(int id);
}
