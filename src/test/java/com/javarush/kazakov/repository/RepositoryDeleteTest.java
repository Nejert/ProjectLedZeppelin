package com.javarush.kazakov.repository;


import com.javarush.kazakov.config.BaseTest;
import com.javarush.kazakov.entity.quest.Quest;
import com.javarush.kazakov.entity.user.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class RepositoryDeleteTest extends BaseTest {
    @BeforeEach
    public void setUp() {
        BaseTest.init();
    }

    @Test
    public void deleteQuestTest() {
        String testQuestName = "JavaRush Quest";
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            QuestRepository<Quest> questRepository = new QuestRepository<>(Quest.class);
            Quest quest = questRepository.get(testQuestName).get();
            questRepository.delete(quest);
            transaction.commit();
        }

        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            QuestRepository<Quest> questRepository = new QuestRepository<>(Quest.class);
            Assertions.assertThrows(NoSuchElementException.class, () -> questRepository.get(testQuestName).get());
            transaction.commit();
        }
    }

    @Test
    public void deleteUserTest() {
        String loginPass = "admin";
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UserRepository userRepository = new UserRepository();
            User admin = userRepository.get(loginPass, loginPass).get();
            userRepository.delete(admin);
            transaction.commit();
        }
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UserRepository userRepository = new UserRepository();
            Assertions.assertThrows(NoSuchElementException.class, () -> userRepository.get(loginPass, loginPass).get());
            transaction.commit();
        }
    }

    @AfterEach
    public void tearDown() {
        BaseTest.destroy();
    }
}
