package com.javarush.kazakov.service;

import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.entity.UserRole;
import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.exception.QuestSQLException;
import com.javarush.kazakov.repository.DB;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserService {

    public List<User> getAll() {
        log.trace("Getting list of all users");
        String sql = "SELECT LOGIN FROM USERS.USER_";
        List<User> users = new ArrayList<>();
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String login = resultSet.getString(1);
                log.trace("Returned login '{}'", login);
                User user = this.get(login);
                log.trace("Adding user '{}' to list", user);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching users", e);
        }
        log.trace("Returning users list '{}'", users);
        return users;
    }

    public int countCreatedQuests(User user) {
        log.trace("Counting creating quests by user '{}'", user);
        String sql = """
                SELECT COUNT(*) FROM QUESTS.QUEST
                WHERE AUTHOR_ID = (SELECT ID FROM USERS.USER_ WHERE LOGIN = ?)
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int quantity = resultSet.getInt(1);
                log.trace("Returning quantity '{}'", quantity);
                return quantity;
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at counting %s's quests".formatted(user.getLogin()), e);
        }
        return 0;
    }

    public User get(String username) {
        log.trace("Getting user by username '{}'", username);
        String sql = """
                SELECT U.ID, U.LOGIN, U.PASSWORD, R.ROLE, U.VICTORY, U.DEFEAT, U.IMAGE, COUNT(Q.ID) AS QUANT
                FROM (USERS.USER_ AS U JOIN USERS.ROLE AS R ON U.ROLE_ID = R.ID)
                         LEFT JOIN QUESTS.QUEST AS Q ON U.ID = Q.AUTHOR_ID
                GROUP BY U.LOGIN
                HAVING U.LOGIN = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String login = resultSet.getString("LOGIN");
                String password = resultSet.getString("PASSWORD");
                UserRole role = UserRole.valueOf(resultSet.getString("ROLE"));
                int victory = resultSet.getInt("VICTORY");
                int defeat = resultSet.getInt("DEFEAT");
                String image = resultSet.getString("IMAGE");
                int quantity = resultSet.getInt("QUANT");
                log.trace("Returned login '{}', password '{}', role '{}', victory '{}', defeat '{}', image '{}', quest quantity '{}'", login, password, role.name(), victory, defeat, image, quantity);
                User user = User.builder()
                        .login(login)
                        .password(password)
                        .role(role)
                        .victory(victory)
                        .defeat(defeat)
                        .image(image)
                        .questQuantity(quantity)
                        .build();
                log.trace("Returning user '{}'", user);
                return user;
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching '%s' user info".formatted(username), e);
        }
        return null;
    }

    public void create(User user) {
        log.trace("Creating '{}' user in database", user);
        checkUniqueness(user);
        String sql = """
                INSERT INTO USERS.USER_(LOGIN, PASSWORD, ROLE_ID, VICTORY, DEFEAT, IMAGE)
                VALUES (?, ?, SELECT ID FROM USERS.ROLE WHERE ROLE = ?, 0, 0, ?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.setString(4, user.getImage());
            log.trace("Executing statement '{}'", statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at creating '%s' user".formatted(user.getLogin()), e);
        }
    }

    private void checkUniqueness(User user) {
        log.trace("Checking uniqueness of database user record");
        String sql = "SELECT * FROM USERS.USER_ WHERE LOGIN = ? OR PASSWORD = ?";
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                throw new QuestException("User '%s' already exists".formatted(user.getLogin()));
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at checking '%s' user".formatted(user.getLogin()), e);
        }
    }

    public void update(User user) {
        log.trace("Updating user '{}'", user);
        String sql = """
                UPDATE USERS.USER_
                SET LOGIN = ?,
                PASSWORD = ?,
                ROLE_ID = (SELECT ID FROM USERS.ROLE WHERE ROLE = ?),
                VICTORY = ?, DEFEAT = ?, IMAGE = ?
                WHERE LOGIN = ? OR PASSWORD = ?;
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().name());
            statement.setInt(4, user.getVictory());
            statement.setInt(5, user.getDefeat());
            statement.setString(6, user.getImage());
            statement.setString(7, user.getLogin());
            statement.setString(8, user.getPassword());
            log.trace("Executing statement '{}'", statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at updating '%s' user".formatted(user.getLogin()), e);
        }
    }

    public void delete(User user) {
        log.trace("Deletting user '{}'", user);
        String sql = "DELETE FROM USERS.USER_ WHERE LOGIN = ?";
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            log.trace("Executing statement '{}'", statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at deleting '%s' user".formatted(user.getLogin()), e);
        }
    }
}
