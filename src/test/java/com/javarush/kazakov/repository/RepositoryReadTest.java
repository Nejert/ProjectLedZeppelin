package com.javarush.kazakov.repository;

import com.javarush.kazakov.config.BaseTest;
import com.javarush.kazakov.entity.quest.Answer;
import com.javarush.kazakov.entity.quest.Quest;
import com.javarush.kazakov.entity.quest.Question;
import com.javarush.kazakov.entity.quest.Result;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class RepositoryReadTest extends BaseTest {
    public static Session session;

    @BeforeAll
    public static void setUp() {
        init();
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();
    }

    @Test
    public void getAllQuestsTest() {
        String expectedQuestTitle = "JavaRush Quest";
        int expectedQuests = 1;
        Repository<Quest> repository = new QuestRepository<>(Quest.class);
        List<Quest> quests = repository.getAll();
        Assertions.assertEquals(expectedQuests, quests.size());
        Assertions.assertEquals(expectedQuests, repository.getCount());
        Assertions.assertEquals(expectedQuestTitle, quests.get(0).getTitle());
    }

    @Test
    public void getAllQuestionsTest() {
        int expectedQuestions = 3;
        Repository<Question> repository = new QuestRepository<>(Question.class);
        List<Question> questions = repository.getAll();
        Assertions.assertEquals(expectedQuestions, questions.size());
        Assertions.assertEquals(expectedQuestions, repository.getCount());
    }

    @Test
    public void getAllAnswersTest() {
        int expectedQuestions = 6;
        Repository<Answer> repository = new QuestRepository<>(Answer.class);
        List<Answer> answers = repository.getAll();
        Assertions.assertEquals(expectedQuestions, answers.size());
        Assertions.assertEquals(expectedQuestions, repository.getCount());
    }

    @Test
    public void getAllResultsTest() {
        int expectedQuestions = 4;
        Repository<Result> repository = new QuestRepository<>(Result.class);
        List<Result> results = repository.getAll();
        Assertions.assertEquals(expectedQuestions, results.size());
        Assertions.assertEquals(expectedQuestions, repository.getCount());
    }

    @Test
    public void getQuestTest() {
        int questId = 1;
        String expectedQuestTitle = "JavaRush Quest";
        String expectedUsername = "admin";
        Repository<Quest> repository = new QuestRepository<>(Quest.class);
        Quest quest = repository.get(questId).get();
        Assertions.assertEquals(expectedQuestTitle, quest.getTitle());
        Assertions.assertEquals(expectedUsername, quest.getAuthor().getLogin());
    }

    @Test
    public void getQuestionTest() {
        int questionId = 1;
        String expectedQuestionTitle = "Ты потерял память. Принять вызов НЛО?";
        Map<Integer, String> expectedAnswers = Map.of(1, "Отклонить вызов", 2, "Принять вызов");
        Repository<Question> repository = new QuestRepository<>(Question.class);
        Question question = repository.get(questionId).get();
        Assertions.assertEquals(expectedQuestionTitle, question.getTitle());
        question.getAnswers()
                .forEach(a-> Assertions.assertEquals(expectedAnswers.get(a.getId()), a.getTitle()));
    }

    @Test
    public void getAnswerTest() {
        int answerId = 1;
        int resultId = 2;
        String expectedTitle = "Отклонить вызов";
        String expectedResultTitle = "Ты отклонил вызов. Поражение";
        Repository<Answer> repository = new QuestRepository<>(Answer.class);
        Answer answer = repository.get(answerId).get();
        Result result = answer.getResult();
        Assertions.assertEquals(expectedTitle, answer.getTitle());
        Assertions.assertEquals(resultId, result.getId());
        Assertions.assertEquals(expectedResultTitle, result.getTitle());
        Assertions.assertFalse(result.getVictory());
    }

    @Test
    public void getResultTest() {
        int resultId = 1;
        String expectedTitle = "Тебя вернули домой. Победа";
        Repository<Result> repository = new QuestRepository<>(Result.class);
        Result result = repository.get(resultId).get();
        Assertions.assertEquals(expectedTitle, result.getTitle());
        Assertions.assertTrue(result.getVictory());
    }
//todo:getUser
    @AfterAll
    public static void tearDown() {
        session.getTransaction().commit();
        session.close();
        destroy();
    }
}
