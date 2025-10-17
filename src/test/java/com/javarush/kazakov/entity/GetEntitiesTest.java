package com.javarush.kazakov.entity;

import com.javarush.kazakov.config.BaseTest;
import com.javarush.kazakov.config.SessionFactory;
import com.javarush.kazakov.entity.quest.Answer;
import com.javarush.kazakov.entity.quest.Quest;
import com.javarush.kazakov.entity.quest.Question;
import com.javarush.kazakov.entity.quest.Result;
import com.javarush.kazakov.entity.user.User;
import org.hibernate.Session;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GetEntitiesTest extends BaseTest {
    public static Session session;

    @BeforeAll
    public static void setUp() {
        init();
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();
    }

    @Test
    public void getAnswerTest() {
        int answerId = 1;
        int resultId = 2;
        Answer answer = session.find(Answer.class, answerId);
        Result result = answer.getResult();
        Assertions.assertNotNull(answer);
        Assertions.assertInstanceOf(HibernateProxy.class, result);
        Assertions.assertEquals(resultId, result.getId());
    }

    @Test
    public void getAnswerWithNoProxyTest() {
        int answerId = 1;
        int resultId = 2;
            Answer answer = session.createQuery("select a from Answer a join fetch a.result where a.id = :id", Answer.class)
                    .setParameter("id", answerId)
                    .uniqueResult();
            Result result = answer.getResult();
            Assertions.assertNotNull(answer);
            Assertions.assertInstanceOf(Result.class, result);
            Assertions.assertEquals(resultId, result.getId());
    }

    @Test
    public void getQuestionTest() {
        try (Session session = SessionFactory.getSessionFactory().openSession()){
            Question question = session.find(Question.class, 1);
            Assertions.assertNotNull(question);
        }
    }

    @Test
    public void getQuestTest() {
        try (Session session = SessionFactory.getSessionFactory().openSession()){
            Quest quest = session.find(Quest.class, 1);
            Assertions.assertNotNull(quest);
        }
    }

    @Test
    public void getResultTest() {
        try (Session session = SessionFactory.getSessionFactory().openSession()){
            Result result = session.get(Result.class, 1);
            Assertions.assertNotNull(result);
        }
    }

    @Test
    public void getUserTest() {
        try (Session session = SessionFactory.getSessionFactory().openSession()){
            User user = session.find(User.class, 1);
            Assertions.assertNotNull(user);
        }
    }
    
    @AfterAll
    public static void tearDown() {
        session.getTransaction().commit();
        session.close();
        destroy();
    }
}
