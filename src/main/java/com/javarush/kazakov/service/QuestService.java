package com.javarush.kazakov.service;

import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.exception.QuestSQLException;
import com.javarush.kazakov.repository.DB;
import com.javarush.kazakov.repository.QuestReader;
import com.javarush.kazakov.repository.QuestWriter;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuestService {

    public Map<String, String> getQuestAuthorMap() {
        String sql = """
                SELECT Q.TITLE, U.LOGIN
                FROM QUESTS.QUEST AS Q LEFT JOIN USERS.USER_ AS U
                ON Q.AUTHOR_ID = U.ID
                """;
        Map<String, String> quests = new LinkedHashMap<>();
        try (Connection connection = DB.getConnection()) {
            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                quests.put(resultSet.getString(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching quest-author map", e);
        }
        return quests;
    }

    public Quest get(String questName) {
        return new QuestReader().read(questName);
    }

    public void create(User user, Quest quest) {
        QuestWriter writer = new QuestWriter();
        writer.write(user, quest);
    }

    public void update(Quest quest) {

    }
    public void delete(Quest quest) {

    }
}
