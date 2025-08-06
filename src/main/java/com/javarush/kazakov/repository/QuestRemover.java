package com.javarush.kazakov.repository;


import com.javarush.kazakov.entity.Answer;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.Question;
import com.javarush.kazakov.entity.Result;
import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.exception.QuestSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class QuestRemover {
    private final String questName;

    public QuestRemover(String questName) {
        this.questName = questName;
    }

    public void remove() {
        checkForQuest();
        QuestReader qr = new QuestReader(questName);
        Quest targetQuest = qr.read();
        Question currentQuestion = targetQuest.getCurrentQuestion();
        removeQuestion(currentQuestion);
        removeQuest(targetQuest);
    }

    private void removeQuest(Quest targetQuest) {
        String sql = "DELETE FROM QUESTS.QUEST WHERE TITLE = ?";
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, targetQuest.getText());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at removing quest", e);
        }
    }

    private void removeQuestion(Question currentQuestion) {
        String sql = "DELETE FROM QUESTS.QUESTION WHERE TITLE = ?";
        List<Answer> answers = currentQuestion.getAnswers();
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, currentQuestion.getText());
            statement.executeUpdate();
            answers.forEach(this::removeAnswer);
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at removing question", e);
        }
    }

    private void removeAnswer(Answer answer) {
        String sql = "DELETE FROM QUESTS.ANSWER WHERE TITLE = ?";
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, answer.getText());
            statement.executeUpdate();
            if (answer.getNextQuestion() != null) {
                removeQuestion(answer.getNextQuestion());
            } else if (answer.getEndResult() != null) {
                removeResult(answer.getEndResult());
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at removing answer", e);
        }
    }

    private void removeResult(Result result) {
        String sql = "DELETE FROM QUESTS.RESULT WHERE TITLE = ?";
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, result.getText());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at removing result", e);
        }
    }

    private void checkForQuest() {
        String sql = "SELECT * FROM QUESTS.QUEST WHERE TITLE = ?";
        try (Connection connection = DB.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, questName);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new QuestException("Quest \"%s\" is not in the database".formatted(questName));
            }
        } catch (SQLException e) {
            throw new QuestSQLException("SQL Error at selecting quest", e);
        }
    }
}
