package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private static int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

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
    public User getUserById(int id) {
        log.info("Getting user with id {}", id);
        if (!isUserExists(id)) {
            throw new NotExistObjectException("User not exist");
        }
        return users.get(id);
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
    public boolean isUserExists(int id) {
        log.info("Checking user with id {} existence", id);
        return users.containsKey(id);
    }
}
