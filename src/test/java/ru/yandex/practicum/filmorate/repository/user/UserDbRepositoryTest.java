package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private UserDbRepository userDbRepository;
    private User user;
    private User friendUser;
    private User commonFriend;

    @BeforeEach
    void sutUp() {
        userDbRepository = new UserDbRepository(jdbcTemplate);
        createUsers();
    }

    private void createUsers() {
        user = User.builder()
                .name("Vasya")
                .email("mail@mail.ru")
                .login("Vasvas")
                .birthday(LocalDate.of(2000, 8, 6))
                .friends(new ArrayList<>())
                .build();
        friendUser = User.builder()
                .name("Pashsa")
                .email("pahan@mail.ru")
                .login("pahan")
                .birthday(LocalDate.of(2010, 7, 16))
                .friends(new ArrayList<>())
                .build();
        commonFriend = User.builder()
                .name("Nikitos")
                .email("ninik@mail.ru")
                .login("niko")
                .birthday(LocalDate.of(2004, 1, 21))
                .friends(new ArrayList<>())
                .build();
    }


    @Test
    void getAllUsers() {
        userDbRepository.addUser(user);
        userDbRepository.addUser(friendUser);
        userDbRepository.addUser(commonFriend);

        List<User> allUsers = userDbRepository.getAllUsers();

        assertTrue(allUsers.size() == 3);
        assertThat(allUsers.contains(user));
        assertThat(allUsers.contains(friendUser));
        assertThat(allUsers.contains(commonFriend));
    }

    @Test
    void getUserById() {
        User addedUser = userDbRepository.addUser(user);

        User user = userDbRepository.getUserById(addedUser.getId()).get();

        assertThat(user).isEqualTo(user);
    }

    @Test
    void updateUser() {
        User userBeforeUpdate = userDbRepository.addUser(user);
        friendUser.setId(userBeforeUpdate.getId());
        userDbRepository.updateUser(friendUser);

        User userAfter = userDbRepository.getUserById(userBeforeUpdate.getId()).get();

        assertThat(friendUser).isEqualTo(userAfter);
    }

    @Test
    void deleteUserById() {
        userDbRepository.addUser(user);

        List<User> allUsers = userDbRepository.getAllUsers();
        assertThat(allUsers.contains(user));

        userDbRepository.deleteUserById(user.getId());
        List<User> afterRemove = userDbRepository.getAllUsers();

        assertThat(user).isNotIn(afterRemove);
    }

    @Test
    void addFriend() {
        userDbRepository.addUser(user);
        userDbRepository.addUser(friendUser);
        userDbRepository.addFriend(user.getId(), friendUser.getId());
        List<User> friendsByUserId = userDbRepository.getFriendsByUserId(user.getId());

        assertThat(friendsByUserId.get(0).getId()).isEqualTo(friendUser.getId());
    }

    @Test
    void getFriendsByUserId() {
        userDbRepository.addUser(user);
        userDbRepository.addUser(friendUser);
        userDbRepository.addUser(commonFriend);

        userDbRepository.addFriend(user.getId(), friendUser.getId());
        userDbRepository.addFriend(user.getId(), commonFriend.getId());
        List<User> friendsByUserId = userDbRepository.getFriendsByUserId(user.getId());

        assertThat(friendsByUserId.size() == 2);
    }

    @Test
    void deleteFriendById() {
        userDbRepository.addUser(user);
        userDbRepository.addUser(friendUser);
        userDbRepository.addUser(commonFriend);

        userDbRepository.addFriend(user.getId(), friendUser.getId());
        userDbRepository.addFriend(user.getId(), commonFriend.getId());
        List<User> friendsByUserId = userDbRepository.getFriendsByUserId(user.getId());
        assertThat(friendsByUserId.size() == 2);

        userDbRepository.deleteFriendById(user.getId(), friendUser.getId());
        List<User> friendsByUserIdAfter = userDbRepository.getFriendsByUserId(user.getId());

        assertThat(friendsByUserIdAfter.size() == 1);
    }

    @Test
    void getCommonFriends() {
        userDbRepository.addUser(user);
        userDbRepository.addUser(friendUser);
        userDbRepository.addUser(commonFriend);

        userDbRepository.addFriend(user.getId(), friendUser.getId());
        userDbRepository.addFriend(user.getId(), commonFriend.getId());
        userDbRepository.addFriend(friendUser.getId(), commonFriend.getId());

        List<User> commonFriends = userDbRepository.getCommonFriends(user.getId(), friendUser.getId());

        assertThat(commonFriends.get(0)).isEqualTo(commonFriend);
    }
}