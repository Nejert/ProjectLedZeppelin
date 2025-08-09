package com.javarush.kazakov.service;

import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.exception.QuestSQLException;
import com.javarush.kazakov.repository.DB;
import com.javarush.kazakov.repository.QuestReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

//    public List<String> getQuestsList() {
//        List<String> quests = new ArrayList<>();
//        try (Connection connection = DB.getConnection()) {
//            ResultSet resultSet = connection.prepareStatement("SELECT * FROM QUESTS.QUEST").executeQuery();
//            while (resultSet.next()) {
//                quests.add(resultSet.getString(2));
//            }
//        } catch (SQLException e) {
//            throw new QuestSQLException("SQL Error at fetching quests list", e);
//        }
//        return quests;
//    }


    public Quest get(String questName) {
        return new QuestReader().read(questName);
    }

    public void create(Quest quest) {
    }

    public void update(Quest quest) {

    }
    public void delete(Quest quest) {

    }
}
