package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotExistObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Slf4j
public class UserDbRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name_user"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(getFriendsByUserId(rs.getInt("user_id")))
                .build();
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("email", user.getEmail());
        userAttributes.put("login", user.getLogin());
        userAttributes.put("name_user", user.getName());
        userAttributes.put("birthday", user.getBirthday());
        return userAttributes;
    }

    @Autowired
    public UserDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM User_Filmorate";
        log.info("All users returned from DB");
        return jdbcTemplate.query(query, this::mapToUser);
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("User_Filmorate")
                .usingGeneratedKeyColumns("user_id");
        Number key = simpleJdbcInsert.executeAndReturnKey(userToMap(user));
        user.setId((Integer) key);
        log.info("User with ID {} saved.", user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        String query = "SELECT * FROM User_Filmorate WHERE user_id=?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, this::mapToUser, id));
    }

    @Override
    public User updateUser(User user) {
        String query = "UPDATE User_Filmorate SET email=?, login=?, name_user=?, birthday=? WHERE user_id=?";
        int userId = user.getId();
        int updateResult = jdbcTemplate.update(query,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                userId);
        if (updateResult > 0) {
            log.info("User with ID {} has been updated.", userId);
        } else {
            throw new NotExistObjectException("User not founded for update by ID=" + userId);
        }
        return user;
    }

    @Override
    public void deleteUserById(int id) {
        String query = "DELETE FROM User_Filmorate WHERE user_id=?";
        int deleteResult = jdbcTemplate.update(query, id);
        if (deleteResult > 0) {
            log.info("User with ID {} has been removed.", id);
        } else {
            log.info("User with ID {} has not been deleted.", id);
        }
    }

    @Override
    public List<User> getFriendsByUserId(long id) {
        String query = "SELECT uf.user_id, uf.email, uf.login, uf.name_user, uf.birthday " +
                "FROM User_Filmorate uf " +
                "JOIN Friendship f ON uf.user_id = f.friend_id " +
                "WHERE f.user_id = ?";
        log.info("All friends of user by ID {} returned from DB", id);
        return jdbcTemplate.query(query, this::mapToUser, id);
    }

    @Override
    public void deleteFriendById(int userId, int idFriend) {
        String query = "DELETE FROM Friendship WHERE user_id=? AND friend_id=?";
        int deleteResult = jdbcTemplate.update(query, userId, idFriend);
        if (deleteResult > 0) {
            log.info("User with ID {} has been removed from friends of user by ID {}.", userId, idFriend);
        } else {
            log.info("Users are not friends");
        }
    }

    @Override
    public void addFriend(int userId, int idFriend) {
        if (userId <= 0 || idFriend <= 0) {
            throw new NotExistObjectException("Users with same id not exists");
        }
        String query = "INSERT INTO Friendship (user_id, friend_id) " +
                "SELECT ?, ? " +
                "WHERE NOT EXISTS ( " +
                "SELECT 1 FROM Friendship " +
                "WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?))";
        int insertResult = jdbcTemplate.update(query, userId, idFriend, userId, idFriend, idFriend, userId);
        if (insertResult > 0) {
            log.info("User with ID {} has been added in friends of user by ID {}.", idFriend, userId);
        }
    }

    @Override
    public List<User> getCommonFriends(int userId, int idFriend) {
        List<User> commonFriends = new ArrayList<>();
        String query = "SELECT u.* FROM Friendship f1 " +
                "INNER JOIN Friendship f2 ON f1.friend_id = f2.friend_id " +
                "INNER JOIN User_Filmorate u ON f1.friend_id = u.user_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ? AND f1.friend_id = f2.friend_id";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(query, userId, idFriend);
        while (sqlRowSet.next()) {
            int friendId = sqlRowSet.getInt("user_id");
            commonFriends.add(getUserById(friendId)
                    .orElseThrow(() -> new NoSuchElementException("Common friend not exist in DB with ID=" + friendId)));
        }
        return commonFriends;
    }
}
