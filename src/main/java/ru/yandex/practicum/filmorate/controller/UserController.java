package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController()
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User addNewUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            throw new ValidationException("User with same id=" + user.getId() + " already exist");
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    private User updateUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }

}
