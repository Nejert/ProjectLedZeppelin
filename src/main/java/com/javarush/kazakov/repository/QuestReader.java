package com.javarush.kazakov.repository;

import com.javarush.kazakov.entity.Answer;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.Question;
import com.javarush.kazakov.entity.Result;
import com.javarush.kazakov.exception.QuestSQLException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class QuestReader {

    public Quest read(String questName) {
        log.trace("Reading quest '{}'", questName);
        String dbQuestName = getDBQuestName(questName.toLowerCase());
        Quest quest = null;
        if (dbQuestName != null) {
            quest = new Quest(dbQuestName, getQuestion(getFirstQuestionId(dbQuestName)));
            log.trace("Returns quest '{}'", quest.getQuestName());
        } else {
            log.warn("No quest found for '{}'", questName);
        }
        return quest;
    }

    private String getDBQuestName(String questName) {
        log.trace("Getting quest name from database");
        String sql = """
                SELECT TITLE FROM QUESTS.QUEST WHERE LOWER(TITLE) LIKE ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, questName);
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString("TITLE");
                log.trace("Returning value '{}'", title);
                return title;
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching db quest's name", e);
        }
        return null;
    }

    private int getFirstQuestionId(String dbQuestName) {
        log.trace("Getting current (first/start) question id from database for quest {}", dbQuestName);
        String sql = """
                SELECT QUESTION_ID
                FROM QUESTS.QUEST JOIN QUESTS.QUEST_FIRST_QUESTION
                ON QUESTS.QUEST.ID = QUESTS.QUEST_FIRST_QUESTION.QUEST_ID
                WHERE TITLE = ?
                """;

        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dbQuestName);
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                log.trace("Returning question id '{}'", id);
                return id;
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching first question", e);
        }
        return -1;
    }

    private Question getQuestion(int id) {
        log.trace("Getting question text by id '{}'", id);
        String sql = """
                SELECT TITLE
                FROM QUESTS.QUESTION
                WHERE ID = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString(1);
                log.trace("Returned value '{}'", title);
                Question question = new Question(title, getAnswers(id));
                log.debug("Returning question object: '{}'", question);
                return question;
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching question", e);
        }
        return null;
    }

    private List<Answer> getAnswers(int id) {
        log.trace("Getting list of answers by question id '{}'", id);
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
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int answerId = resultSet.getInt(1);
                log.trace("Returned answer id '{}'", answerId);
                String text = resultSet.getString(2);
                log.trace("Returned answer text '{}'", text);
                String nextQuestionId = resultSet.getString(3);
                log.trace("Returned next question id '{}'", nextQuestionId);
                Answer answer;
                if (nextQuestionId != null) {
                    int nextId = Integer.parseInt(nextQuestionId);
                    answer = new Answer(text, getQuestion(nextId), null);
                } else {
                    answer = new Answer(text, null, getResult(answerId));
                }
                log.trace("Adding answer to answers list: {}", answer);
                answers.add(answer);
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching answers", e);
        }
        log.trace("Returning answers list: {}", answers);
        return answers;
    }

    private Result getResult(int id) {
        log.trace("Getting result text by answer id '{}'", id);
        String sql = """
                SELECT TITLE, VICTORY
                FROM QUESTS.RESULT JOIN QUESTS.ANSWER_RESULT
                ON RESULT.ID = ANSWER_RESULT.RESULT_ID
                WHERE ANSWER_ID = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String title = resultSet.getString(1);
                log.trace("Returned result text '{}'", title);
                boolean isVictory = resultSet.getBoolean(2);
                log.trace("Returned result victory flag '{}'", isVictory);
                Result result = new Result(title, isVictory);
                log.trace("Returning result '{}'", result);
                return result;
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at fetching end result", e);
        }
        return null;
    }
}
