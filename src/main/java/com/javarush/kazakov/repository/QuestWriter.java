package com.javarush.kazakov.repository;

import com.javarush.kazakov.entity.*;
import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.exception.QuestSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestWriter {
    private final Quest quest;

    public QuestWriter(Quest quest) {
        this.quest = quest;
    }

    public void write() {
        String questName = quest.getQuestName();
        Question currentQuestion = quest.getCurrentQuestion();
        checkQuestExists(questName);

        writeQuestName(questName);
        writeQuestion(currentQuestion);
        writeLink(Table.QUEST_FIRST_QUESTION, getDBId(quest), getDBId(currentQuestion));

    }

    private void checkQuestExists(String questName) {
        String sql = """
                SELECT ID FROM QUESTS.QUEST WHERE TITLE = ?
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, questName);
            if (statement.executeQuery().next()) {
                throw new QuestException("This quest already is in the database");
            }
            ;
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at checking quests existence", e);
        }
    }

    private void writeQuestName(String questName) {
        String sql = """
                INSERT INTO QUESTS.QUEST (TITLE)
                VALUES (?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, questName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at writing quests name", e);
        }
    }

    private void writeQuestion(Question question) {
        String sql = """
                INSERT INTO QUESTS.QUESTION (TITLE)
                VALUES (?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, question.getText());
            statement.executeUpdate();
            int questionId = getDBId(question);
            writeAnswers(questionId, question.getAnswers());
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at writing question", e);
        }
    }

    private void writeAnswers(int questionId, List<Answer> answers) {
        String sql = """
                INSERT INTO QUESTS.ANSWER(TITLE)
                VALUES (?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (Answer answer : answers) {
                statement.setString(1, answer.getText());
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
        String sql = """
                INSERT INTO QUESTS.RESULT(TITLE)
                VALUES (?);
                """;
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, result.getText());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at writing result", e);
        }
    }

    private void writeLink(Table table, int first, int second) {
        String firstColumn = table.getColumns()[0];
        String secondColumn = table.getColumns()[1];
        String sql = """
                INSERT INTO QUESTS.%s (%s, %s)
                VALUES (%d, %d);
                """.formatted(table.name(), firstColumn, secondColumn,
                first, second);
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            String firstColName = firstColumn.substring(0, firstColumn.indexOf('_')).toLowerCase();
            String secondColName = secondColumn.substring(0, firstColumn.indexOf('_')).toLowerCase();
            throw new QuestSQLException("SQL Error at linkage %s and %s".formatted(firstColName, secondColName), e);
        }
    }

    private int getDBId(QuestEntity entity) {
        String className = entity.getClass().getSimpleName().toLowerCase();
        String sql = """
                SELECT ID FROM QUESTS.%s WHERE TITLE = '%s'
                """.formatted(className, entity.getText());
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Unable to fetch db %s id".formatted(className), e);
        }
        return -1;
    }
}
