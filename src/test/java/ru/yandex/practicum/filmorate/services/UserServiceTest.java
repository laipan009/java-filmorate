package ru.yandex.practicum.filmorate.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    private User user;
    private User someUser;
    private User defaultUser;

    @BeforeEach
    void setUp() {
        user = createUser(1, "test@test.com", "test", "Test User", LocalDate.of(1990, 1, 1));
        someUser = createUser(2, "test2@test.com", "test2", "Test User2", LocalDate.of(1992, 2, 2));
        defaultUser = createUser(2, "test2@test.com", "test2", "Test User2", LocalDate.of(1992, 2, 2));
    }

    @Test
    void isUserExists_WhenCreateUserThenAddToDBReturnTrue() {
        userService.userRepository.addUser(user);
        assertThat(userService.userRepository.isUserExists(user.getId())).isTrue();
    }

    @Test
    void isUsersAlreadyFriends_WhenCreateTwoUsersThenAddEachOtherAsFriendsReturnTrue() {
        userService.userRepository.addUser(user);
        userService.userRepository.addUser(someUser);

        userService.addFriend(user.getId(), someUser.getId());

        assertTrue(userService.isUsersAlreadyFriends(user.getId(), someUser.getId()));
    }

    @Test
    void addFriend_WhenUserNotHaveFriendThenAddFriendReturnTrue() {
        userService.userRepository.addUser(user);
        assertTrue(userService.getFriendsByIdUser(user.getId()).isEmpty());
        userService.userRepository.addUser(someUser);

        userService.addFriend(user.getId(), someUser.getId());

        assertTrue(userService.isUsersAlreadyFriends(user.getId(), someUser.getId()));
    }

    @Test
    void deleteFriend_WhenUserHaveFriendThenDeleteFriendReturnFalse() {
        userService.userRepository.addUser(user);
        userService.userRepository.addUser(someUser);
        userService.addFriend(user.getId(), someUser.getId());
        assertTrue(userService.isUsersAlreadyFriends(user.getId(), someUser.getId()));

        userService.deleteFriend(user.getId(), someUser.getId());

        assertFalse(userService.isUsersAlreadyFriends(user.getId(), someUser.getId()));
    }

    @Test
    void getFriendsByIdUser_WhenUserHaveFriendThenGetFriendReturnTrue() {
        userService.userRepository.addUser(user);
        userService.userRepository.addUser(someUser);
        userService.addFriend(user.getId(), someUser.getId());
        assertTrue(userService.isUsersAlreadyFriends(user.getId(), someUser.getId()));

        List<User> friendsByIdUser = userService.getFriendsByIdUser(user.getId());

        assertTrue(friendsByIdUser.contains(someUser));
    }

    @Test
    void getCommonFriends_WhenExistTwoUsersWhichDontHaveCommonFriendsThenAddCommonFriendReturnTrue() {
        userService.userRepository.addUser(user);
        userService.userRepository.addUser(someUser);
        userService.userRepository.addUser(defaultUser);

        userService.addFriend(user.getId(), defaultUser.getId());
        userService.addFriend(someUser.getId(), defaultUser.getId());

        List<User> commonFriends = userService.getCommonFriends(user.getId(), someUser.getId());

        assertTrue(commonFriends.contains(defaultUser));
        assertEquals(1, commonFriends.size());
    }

    private User createUser(int id, String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }
}