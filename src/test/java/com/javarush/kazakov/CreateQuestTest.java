package com.javarush.kazakov;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.entity.*;
import com.javarush.kazakov.repository.DB;
import com.javarush.kazakov.service.QuestService;
import com.javarush.kazakov.service.UserService;
import org.junit.jupiter.api.*;


import static com.javarush.kazakov.TestQuest.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateQuestTest {
    public static Quest testQuest;
    public static Quest DBTestQuest;

    @BeforeAll
    public static void setUp() {
        DB.getInstance().restoreDefaultDBFile();
        testQuest = getTestQuest();
        User admin = Winter.find(UserService.class).get("admin");
        QuestService questService = Winter.find(QuestService.class);
        questService.create(admin, testQuest);
        DBTestQuest = questService.get(QUEST_NAME);
    }


    @Test
    @Order(1)
    public void questNotNullTest() {
        assertNotNull(DBTestQuest);
    }

    @Test
    @Order(2)
    public void checkFirstQuestion() {
        Question currentQuestion = DBTestQuest.getCurrentQuestion();
        assertEquals(FIRST_QUESTION, currentQuestion.getText());
        assertEquals(FIRST_QUESTION_FIRST_FALSE_ANSWER, currentQuestion.getAnswers().get(0).getText());
        assertEquals(FIRST_QUESTION_SECOND_FALSE_ANSWER, currentQuestion.getAnswers().get(1).getText());
        assertEquals(FIRST_QUESTION_TRUE_ANSWER, currentQuestion.getAnswers().get(2).getText());
    }

    @Test
    @Order(3)
    public void checkFirstQuestionResults() {
        Question currentQuestion = DBTestQuest.getCurrentQuestion();
        Answer firstFalseAnswer = currentQuestion.getAnswers().get(0);
        Answer secondFalseAnswer = currentQuestion.getAnswers().get(1);
        Result firstResult = firstFalseAnswer.getEndResult();
        Result secondResult = secondFalseAnswer.getEndResult();
        assertEquals(FAIL_FIRST_QUESTION_FIRST_ANSWER, firstResult.getText());
        assertEquals(FAIL_FIRST_QUESTION_SECOND_ANSWER, secondResult.getText());
    }

    @Test
    @Order(4)
    public void checkSecondQuestion() {
        Question currentQuestion = DBTestQuest.getCurrentQuestion();
        Answer trueAnswer = currentQuestion.getAnswers().get(2);
        assertNull(trueAnswer.getEndResult());
        assertNotNull(trueAnswer.getNextQuestion());
        DBTestQuest.setCurrentQuestion(trueAnswer.getNextQuestion());
        currentQuestion = DBTestQuest.getCurrentQuestion();
        assertEquals(SECOND_QUESTION, currentQuestion.getText());
        assertEquals(SECOND_QUESTION_FIRST_FALSE_ANSWER, currentQuestion.getAnswers().get(0).getText());
        assertEquals(SECOND_QUESTION_SECOND_FALSE_ANSWER, currentQuestion.getAnswers().get(1).getText());
        assertEquals(SECOND_QUESTION_TRUE_ANSWER, currentQuestion.getAnswers().get(2).getText());
    }

    @Test
    @Order(5)
    public void checkSecondQuestionResult() {
        Question currentQuestion = DBTestQuest.getCurrentQuestion();
        Answer firstFalseAnswer = currentQuestion.getAnswers().get(0);
        Answer secondFalseAnswer = currentQuestion.getAnswers().get(1);
        Result firstResult = firstFalseAnswer.getEndResult();
        Result secondResult = secondFalseAnswer.getEndResult();
        assertEquals(FAIL_SECOND_QUESTION_FIRST_ANSWER, firstResult.getText());
        assertEquals(FAIL_SECOND_QUESTION_SECOND_ANSWER, secondResult.getText());
    }

    @Test
    @Order(6)
    public void checkThirdQuestion() {
        Question currentQuestion = DBTestQuest.getCurrentQuestion();
        Answer trueAnswer = currentQuestion.getAnswers().get(2);
        assertNull(trueAnswer.getEndResult());
        assertNotNull(trueAnswer.getNextQuestion());
        DBTestQuest.setCurrentQuestion(trueAnswer.getNextQuestion());
        currentQuestion = DBTestQuest.getCurrentQuestion();
        assertEquals(THIRD_QUESTION, currentQuestion.getText());
        assertEquals(THIRD_QUESTION_FIRST_FALSE_ANSWER, currentQuestion.getAnswers().get(0).getText());
        assertEquals(THIRD_QUESTION_SECOND_FALSE_ANSWER, currentQuestion.getAnswers().get(1).getText());
        assertEquals(THIRD_QUESTION_TRUE_ANSWER, currentQuestion.getAnswers().get(2).getText());
    }

    @Test
    @Order(7)
    public void checkFinalResult() {
        Question currentQuestion = DBTestQuest.getCurrentQuestion();
        Answer firstFalseAnswer = currentQuestion.getAnswers().get(0);
        Answer secondFalseAnswer = currentQuestion.getAnswers().get(1);
        Answer trueAnswer = currentQuestion.getAnswers().get(2);
        Result firstFalseResult = firstFalseAnswer.getEndResult();
        Result secondFalseResult = secondFalseAnswer.getEndResult();
        Result trueResult = trueAnswer.getEndResult();
        assertNull(firstFalseAnswer.getNextQuestion());
        assertNull(secondFalseAnswer.getNextQuestion());
        assertNull(trueAnswer.getNextQuestion());
        assertEquals(FAIL_THIRD_QUESTION_FIRST_ANSWER, firstFalseResult.getText());
        assertEquals(FAIL_THIRD_QUESTION_SECOND_ANSWER, secondFalseResult.getText());
        assertEquals(SUCCEED_THIRD_QUESTION, trueResult.getText());
    }

}
