package ru.yandex.practicum.filmorate.DAO;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDAO {
    List<User> getAllUsers();

    User addUser(User user);

    User getUserById(int id);

    User updateUser(User user);

    User deleteUserById(int id);
}
