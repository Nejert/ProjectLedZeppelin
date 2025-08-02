package com.javarush.kazakov.service;

import com.javarush.kazakov.entity.Answer;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.Question;
import com.javarush.kazakov.entity.Result;
import com.javarush.kazakov.exception.QuestSQLException;
import com.javarush.kazakov.repository.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestReader {
    private final String questName;

    public QuestReader(String questName) {
        this.questName = questName;
    }

    public Quest read() {
        String dbQuestName = getDBQuestName(questName.toLowerCase());
        Quest quest = null;
        if (dbQuestName != null) {
            quest = new Quest(dbQuestName, getQuestion(getFirstQuestionId(dbQuestName)));
        }
        return quest;
    }

    private String getDBQuestName(String questName) {
        String sql = """
                SELECT TITLE FROM QUESTS.QUEST WHERE LOWER(TITLE) LIKE ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, questName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("TITLE");
            }
            ;
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching db quest's name", e);
        }
        return null;
    }

    private boolean isQuestExists(String questName) {
        String sql = """
                SELECT ID FROM QUESTS.QUEST WHERE TITLE = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, questName);
            if (!statement.executeQuery().next()) {
                return false;
            }
            ;
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at checking quests existence", e);
        }
        return true;
    }

    private int getFirstQuestionId(String dbQuestName) {
        String sql = """
                SELECT QUESTION_ID
                FROM QUESTS.QUEST JOIN QUESTS.QUEST_FIRST_QUESTION
                ON QUESTS.QUEST.ID = QUESTS.QUEST_FIRST_QUESTION.QUEST_ID
                WHERE TITLE = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dbQuestName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching first question", e);
        }
        return -1;
    }

    private Question getQuestion(int id) {
        String sql = """
                SELECT TITLE
                FROM QUESTS.QUESTION
                WHERE ID = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Question(resultSet.getString(1), getAnswers(id));
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching question", e);
        }
        return null;
    }

    private List<Answer> getAnswers(int id) {
        String sql = """
                SELECT ID, TITLE, NEXT.QUESTION_ID
                FROM (QUESTS.ANSWER JOIN QUESTS.QUESTION_ANSWER ON QUESTS.ANSWER.ID = QUESTS.QUESTION_ANSWER.ANSWER_ID)
                     LEFT JOIN QUESTS.ANSWER_NEXT_QUESTION AS NEXT ON QUESTS.ANSWER.ID = NEXT.ANSWER_ID
                WHERE QUESTS.QUESTION_ANSWER.QUESTION_ID = ?
                """;
        List<Answer> answers = new ArrayList<>();
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String answer = resultSet.getString(2);
                String nextQuestionId = resultSet.getString(3);
                if (nextQuestionId != null) {
                    int nextId = Integer.parseInt(nextQuestionId);
                    answers.add(new Answer(answer, getQuestion(nextId), null));
                } else {
                    int answerId = Integer.parseInt(resultSet.getString(1));
                    answers.add(new Answer(answer, null, getResult(answerId)));
                }

            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching answers", e);
        }
        return answers;
    }

    private Result getResult(int id) {
        String sql = """
                SELECT TITLE
                FROM QUESTS.RESULT JOIN QUESTS.ANSWER_RESULT
                ON RESULT.ID = ANSWER_RESULT.RESULT_ID
                WHERE ANSWER_ID = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Result(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching end result", e);
        }
        return null;
    }
}
