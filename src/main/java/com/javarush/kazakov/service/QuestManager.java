package com.javarush.kazakov.service;

import com.javarush.kazakov.exception.QuestSQLException;
import com.javarush.kazakov.repository.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class QuestManager {

    public List<String> getQuestsList() {
        List<String> quests = new ArrayList<>();
        try (Connection connection = DB.getConnection()) {
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM QUEST").executeQuery();
            while (resultSet.next()) {
                quests.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching quests list", e);
        }
        return quests;
    }
}
