package com.javarush.kazakov.repository;

import com.javarush.kazakov.entity.*;
import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.exception.QuestSQLException;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class QuestWriter {

    public void write(User user, Quest quest) {
        String questName = quest.getQuestName();
        log.trace("Writing quest '{}' by user '{}'", questName, user.getLogin());
        Question currentQuestion = quest.getCurrentQuestion();
        checkQuestExists(questName);

        writeQuestName(questName);
        writeQuestion(currentQuestion);
        writeLink(Table.QUEST_FIRST_QUESTION, getDBId(quest), getDBId(currentQuestion));
        linkQuestAuthor(questName, user.getLogin());
    }

    private void linkQuestAuthor(String questName, String username) {
        log.trace("Linking quest '{}' with its author '{}'", questName, username);
        String sql = """
                UPDATE QUESTS.QUEST
                SET QUESTS.QUEST.AUTHOR_ID = (SELECT ID FROM USERS.USER_ WHERE LOGIN = ?)
                WHERE QUESTS.QUEST.TITLE = ?;
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, questName);
            log.trace("Executing statement '{}'", statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at updating quest's author", e);
        }
    }

    private void checkQuestExists(String questName) {
        log.trace("Checking quest '{}' presence in database", questName);
        String sql = """
                SELECT ID FROM QUESTS.QUEST WHERE TITLE = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, questName);
            log.trace("Executing statement '{}'", statement);
            if (statement.executeQuery().next()) {
                throw new QuestException("This quest already is in the database");
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at checking quests existence", e);
        }
    }

    private void writeQuestName(String questName) {
        log.trace("Writing quest name '{}' to database", questName);
        String sql = """
                INSERT INTO QUESTS.QUEST (TITLE)
                VALUES (?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, questName);
            log.trace("Executing statement '{}'", statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at writing quest's name", e);
        }
    }

    private void writeQuestion(Question question) {
        log.trace("Writing question '{}'", question);
        String sql = """
                INSERT INTO QUESTS.QUESTION (TITLE)
                VALUES (?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, question.getText());
            log.trace("Executing statement '{}'", statement);
            statement.executeUpdate();
            int questionId = getDBId(question);
            writeAnswers(questionId, question.getAnswers());
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at writing question", e);
        }
    }

    private void writeAnswers(int questionId, List<Answer> answers) {
        log.trace("Writing answers '{}' to question with id '{}'", answers, questionId);
        String sql = """
                INSERT INTO QUESTS.ANSWER(TITLE)
                VALUES (?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (Answer answer : answers) {
                statement.setString(1, answer.getText());
                log.trace("Executing statement '{}'", statement);
                statement.executeUpdate();
                int answerId = getDBId(answer);
                writeLink(Table.QUESTION_ANSWER,questionId, answerId);
                if (answer.getNextQuestion() != null) {
                    writeQuestion(answer.getNextQuestion());
                    int nextQuestionId = getDBId(answer.getNextQuestion());
                    writeLink(Table.ANSWER_NEXT_QUESTION, answerId, nextQuestionId);
                } else if (answer.getEndResult() != null) {
                    writeResult(answer.getEndResult());
                    int resultId = getDBId(answer.getEndResult());
                    writeLink(Table.ANSWER_RESULT, answerId, resultId);
                }
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at writing answer", e);
        }
    }

    private void writeResult(Result result) {
        log.trace("Writing result '{}'", result);
        String sql = """
                INSERT INTO QUESTS.RESULT(TITLE, VICTORY)
                VALUES (?,?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, result.getText());
            statement.setBoolean(2, result.isVictory());
            log.trace("Executing statement '{}'", statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at writing result", e);
        }
    }

    private void writeLink(Table table, int first, int second) {
        String firstColumn = table.getColumns()[0];
        String secondColumn = table.getColumns()[1];
        log.trace("Writing link to '{}' table, to '{}' and '{}' columns, '{}', '{}' values", table, firstColumn, secondColumn, first, second);
        String sql = """
                INSERT INTO QUESTS.%s (%s, %s)
                VALUES (%d, %d);
                """.formatted(table.name(), firstColumn, secondColumn,
                first, second);
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            log.trace("Executing statement '{}'", statement);
            statement.executeUpdate();
        } catch (SQLException e) {
            String firstColName = firstColumn.substring(0, firstColumn.indexOf('_')).toLowerCase();
            String secondColName = secondColumn.substring(0, firstColumn.indexOf('_')).toLowerCase();
            throw new QuestSQLException("SQL Error at linkage %s and %s".formatted(firstColName, secondColName), e);
        }
    }

    private int getDBId(QuestEntity entity) {
        log.trace("Getting quest entity '{}' database id", entity);
        String className = entity.getClass().getSimpleName().toLowerCase();
        String sql = """
                SELECT ID FROM QUESTS.%s WHERE TITLE = ?
                """.formatted(className);
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, entity.getText());
            log.trace("Executing statement '{}'", statement);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                log.trace("Returning '{}' id '{}'", entity, id);
                return id;
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Unable to fetch db %s id".formatted(className), e);
        }
        return -1;
    }
}
