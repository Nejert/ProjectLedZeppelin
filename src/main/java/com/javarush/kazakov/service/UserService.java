package com.javarush.kazakov.service;

import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.entity.UserRole;
import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.exception.QuestSQLException;
import com.javarush.kazakov.repository.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    public User get(String username) {
        String sql = """
                SELECT U.ID, U.LOGIN, U.PASSWORD, ROLE.ROLE, U.VICTORY, U.DEFEAT
                FROM USERS.USER_ AS U JOIN USERS.ROLE
                ON U.ROLE_ID = ROLE.ID
                WHERE LOGIN = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String login = resultSet.getString("LOGIN");
                String password = resultSet.getString("PASSWORD");
                UserRole role = UserRole.valueOf(resultSet.getString("ROLE"));
                int victory = resultSet.getInt("VICTORY");
                int defeat = resultSet.getInt("DEFEAT");
                return new User(login, password, role, victory, defeat);
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching " + username + " user info", e);
        }
        return null;
    }

    public void create(User user) {
        checkUniqueness(user);
        String sql = """
                INSERT INTO USERS.USER_(LOGIN, PASSWORD, ROLE_ID, VICTORY, DEFEAT)
                VALUES (?, ?, SELECT ID FROM USERS.ROLE WHERE ROLE = ?, 0, 0);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.login());
            statement.setString(2, user.password());
            statement.setString(3, user.role().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at creating " + user.login() + " user", e);
        }
    }

    private void checkUniqueness(User user) {
        String sql = """
                SELECT * FROM USERS.USER_ WHERE LOGIN = ? OR PASSWORD = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.login());
            statement.setString(2, user.password());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                throw new QuestException("User already exists");
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at checking " + user.login() + " user", e);
        }
    }

    public void update(User user) {
        String sql = """
                UPDATE USERS.USER_
                SET LOGIN = ?,
                PASSWORD = ?,
                ROLE_ID = (SELECT ID FROM USERS.ROLE WHERE ROLE = ?),
                VICTORY = ?, DEFEAT = ?
                WHERE LOGIN = ? OR PASSWORD = ?;
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.login());
            statement.setString(2, user.password());
            statement.setString(3, user.role().name());
            statement.setInt(4, user.victory());
            statement.setInt(5, user.defeat());
            statement.setString(6, user.login());
            statement.setString(7, user.password());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at updating " + user.login() + " user", e);
        }
    }

    public void delete(User user) {
        String sql = "DELETE FROM USERS.USER_ WHERE LOGIN = ?";
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.login());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at deleting " + user.login() + " user", e);
        }
    }
}
