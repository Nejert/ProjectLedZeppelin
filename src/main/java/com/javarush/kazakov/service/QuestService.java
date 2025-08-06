package com.javarush.kazakov.service;

import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.exception.QuestSQLException;
import com.javarush.kazakov.repository.DB;
import com.javarush.kazakov.repository.QuestReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestService {

    public List<String> getQuestsList() {
        List<String> quests = new ArrayList<>();
        try (Connection connection = DB.getConnection()) {
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM QUESTS.QUEST").executeQuery();
            while (resultSet.next()) {
                quests.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching quests list", e);
        }
        return quests;
    }

    public Quest get(String questName) {
        return new QuestReader(questName).read();
    }

    public void create(Quest quest) {
    }

    public void update(Quest quest) {

    }
    public void delete(Quest quest) {

    }
}
