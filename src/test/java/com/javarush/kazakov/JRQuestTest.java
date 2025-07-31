package com.javarush.kazakov;

import com.javarush.kazakov.entity.Answer;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.Question;
import com.javarush.kazakov.entity.Result;
import com.javarush.kazakov.repository.DB;
import com.javarush.kazakov.service.QuestReader;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JRQuestTest {
    public static Quest quest;

    @BeforeAll
    public static void setUp() {
        DB.getInstance();
        quest = new QuestReader("JavaRush Quest").read();
    }

    @Test
    @Order(1)
    public void fetchingJavaRushQuestFromDb() {
        assertNotNull(quest);
    }

    @Test
    @Order(2)
    public void checkFirstQuestion() {
        Question currentQuestion = quest.getCurrentQuestion();
        assertEquals("Ты потерял память. Принять вызов НЛО?", currentQuestion.getText());
        assertEquals("Отклонить вызов", currentQuestion.getAnswers().get(0).getText());
        assertEquals("Принять вызов", currentQuestion.getAnswers().get(1).getText());
    }

    @Test
    @Order(3)
    public void checkFirstQuestionResult() {
        Question currentQuestion = quest.getCurrentQuestion();
        Answer falseAnswer = currentQuestion.getAnswers().get(0);
        Result result = falseAnswer.getEndResult();
        assertNull(falseAnswer.getNextQuestion());
        assertEquals("Ты отклонил вызов. Поражение", result.getText());
    }

    @Test
    @Order(4)
    public void checkSecondQuestion() {
        Question currentQuestion = quest.getCurrentQuestion();
        Answer trueAnswer = currentQuestion.getAnswers().get(1);
        assertNull(trueAnswer.getEndResult());
        assertNotNull(trueAnswer.getNextQuestion());
        quest.setCurrentQuestion(trueAnswer.getNextQuestion());
        currentQuestion = quest.getCurrentQuestion();
        assertEquals("Ты принял вызов. Поднимаешься на мостик к капитану?", currentQuestion.getText());
        assertEquals("Отказаться подниматься на мостик", currentQuestion.getAnswers().get(0).getText());
        assertEquals("Подняться на мостик", currentQuestion.getAnswers().get(1).getText());
    }

    @Test
    @Order(5)
    public void checkSecondQuestionResult() {
        Question currentQuestion = quest.getCurrentQuestion();
        Answer falseAnswer = currentQuestion.getAnswers().get(0);
        Result result = falseAnswer.getEndResult();
        assertNull(falseAnswer.getNextQuestion());
        assertEquals("Ты не пошел на переговоры. Поражение", result.getText());
    }

    @Test
    @Order(6)
    public void checkThirdQuestion() {
        Question currentQuestion = quest.getCurrentQuestion();
        Answer trueAnswer = currentQuestion.getAnswers().get(1);
        assertNull(trueAnswer.getEndResult());
        assertNotNull(trueAnswer.getNextQuestion());
        quest.setCurrentQuestion(trueAnswer.getNextQuestion());
        currentQuestion = quest.getCurrentQuestion();
        assertEquals("Ты поднялся на мостик. Ты кто?", currentQuestion.getText());
        assertEquals("Солгать о себе", currentQuestion.getAnswers().get(0).getText());
        assertEquals("Рассказать правду о себе", currentQuestion.getAnswers().get(1).getText());
    }

    @Test
    @Order(7)
    public void checkFinalResult() {
        Question currentQuestion = quest.getCurrentQuestion();
        Answer falseAnswer = currentQuestion.getAnswers().get(0);
        Answer trueAnswer = currentQuestion.getAnswers().get(1);
        Result falseResult = falseAnswer.getEndResult();
        Result trueResult = trueAnswer.getEndResult();
        assertNull(falseAnswer.getNextQuestion());
        assertNull(trueAnswer.getNextQuestion());
        assertEquals("Твою ложь разоблачили. Поражение", falseResult.getText());
        assertEquals("Тебя вернули домой. Победа", trueResult.getText());
    }
}
