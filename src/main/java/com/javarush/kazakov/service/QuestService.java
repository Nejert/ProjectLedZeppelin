package com.javarush.kazakov.service;

import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.exception.QuestSQLException;
import com.javarush.kazakov.repository.DB;
import com.javarush.kazakov.repository.QuestReader;
import com.javarush.kazakov.repository.QuestRemover;
import com.javarush.kazakov.repository.QuestWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class QuestService {
    QuestReader questReader;
    QuestWriter questWriter;
    QuestRemover questRemover;

    public Map<String, String> getQuestAuthorMap() {
        log.trace("Getting quest-author map");
        String sql = """
                SELECT Q.TITLE, U.LOGIN
                FROM QUESTS.QUEST AS Q LEFT JOIN USERS.USER_ AS U
                ON Q.AUTHOR_ID = U.ID
                """;
        Map<String, String> quests = new LinkedHashMap<>();
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            log.trace("Executing statement '{}'", statement.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String login = resultSet.getString(2);
                log.trace("Putting map entry with title '{}' -> login '{}'", title, login);
                quests.put(title, login);
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching quest-author map", e);
        }
        log.trace("Returning map '{}'", quests);
        return quests;
    }

    public Quest get(String questName) {
        return questReader.read(questName);
    }

    public void create(User user, Quest quest) {
        questWriter.write(user, quest);
    }

    public void update(User user, Quest quest) {
        questRemover.remove(quest.getQuestName());
        questWriter.write(user, quest);
    }
    public void delete(Quest quest) {
        questRemover.remove(quest.getQuestName());
    }
}
