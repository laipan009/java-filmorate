package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("GET request received to receive all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("GET request received to receive user by id=" + id);
        return userService.getUserById(id);
    }

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        log.info("POST request received to add user:" + user);
        return userService.addUser(user);
    }

    @PutMapping
    private User updateUser(@Valid @RequestBody User user) {
        log.info("PUT request received to update user:" + user);
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("PUT request received to add friend from user by id=" + id + " to user by id=" + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("DELETE request received to remove friend from user by id=" + id + " to user by id=" + friendId);
        userService.deleteFriendById(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsByIdUser(@PathVariable int id) {
        log.info("GET request received to receive friend list user by id=" + id);
        return userService.getFriendsByIdUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("GET request received to receive common friend list between user by id=" + id + " and user by id=" + otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
