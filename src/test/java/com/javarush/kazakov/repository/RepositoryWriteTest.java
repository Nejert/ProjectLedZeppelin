package com.javarush.kazakov.repository;

import com.javarush.kazakov.config.BaseTest;
import com.javarush.kazakov.entity.quest.Quest;
import com.javarush.kazakov.entity.quest.Question;
import com.javarush.kazakov.entity.user.User;
import com.javarush.kazakov.object.TestQuest;
import com.javarush.kazakov.object.TestUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RepositoryWriteTest extends BaseTest {

    @BeforeAll
    public static void setUp() {
        init();
    }
    //    save
    @Test
    public void saveQuestTest() {
        String testQuestName;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            Quest quest = TestQuest.getTestQuest();
            testQuestName = quest.getTitle();
            Repository<Quest> questRepository = new QuestRepository<>(Quest.class);
            Repository<User> userRepository = new UserRepository();
            User admin = userRepository.get(1).get();
            quest.setAuthor(admin);
            questRepository.save(quest);
            transaction.commit();
        }

        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            Quest quest = new QuestRepository<>(Quest.class).get(testQuestName).get();
            Assertions.assertEquals(TestQuest.QUEST_NAME, quest.getTitle());
            Assertions.assertEquals(TestQuest.FIRST_QUESTION, quest.getFirstQuestion().getTitle());
            Assertions.assertEquals(TestQuest.FIRST_QUESTION_FIRST_FALSE_ANSWER, quest.getFirstQuestion().getAnswers().get(0).getTitle());
            transaction.commit();
        }
    }

    @Test
    public void saveUserTest() {
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            Repository<User> userRepository = new UserRepository();
            userRepository.save(TestUser.TEST.getUser());
            transaction.commit();
        }

        int expectedId = 3;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            Repository<User> userRepository = new UserRepository();
            User testUser = TestUser.TEST.getUser();
            User user = userRepository.get(testUser.getLogin(), testUser.getPassword()).get();
            Assertions.assertEquals(expectedId, user.getId());
            Assertions.assertEquals(testUser.getLogin(), user.getLogin());
            Assertions.assertEquals(testUser.getPassword(), user.getPassword());
            Assertions.assertEquals(testUser.getRole(), user.getRole());
            Assertions.assertEquals(testUser.getImage(), user.getImage());
            transaction.commit();
        }

    }

    //    update
    @Test
    public void updateQuestTest() {
        String testQuestName = "JavaRush Quest";
        String expectedTestQuestName = "JavaRush Quest test";
        String expectedFirstQuestionTitle = "Ты потерял память. Принять вызов НЛО? test";
        int testQuestId;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            QuestRepository<Quest> questRepository = new QuestRepository<>(Quest.class);
            Quest quest = questRepository.get(testQuestName).get();
            testQuestId = quest.getId();
            quest.setTitle(testQuestName + " test");
            Question firstQuestion = quest.getFirstQuestion();
            firstQuestion.setTitle(firstQuestion.getTitle() + " test");
            questRepository.update(quest);
            transaction.commit();
        }

        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            Quest quest = new QuestRepository<>(Quest.class).get(testQuestId).get();
            Assertions.assertEquals(expectedTestQuestName, quest.getTitle());
            Assertions.assertEquals(expectedFirstQuestionTitle, quest.getFirstQuestion().getTitle());
            transaction.commit();
        }
    }

    @Test
    public void updateUserTest() {
        String userLoginPass = "admin";
        String newLoginPass = "Admin";
        int userId;
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UserRepository userRepository = new UserRepository();
            User user = userRepository.get(userLoginPass, userLoginPass).get();
            userId = user.getId();
            user.setLogin(newLoginPass);
            user.setPassword(newLoginPass);
            userRepository.update(user);
            transaction.commit();
        }
        try (Session session = sessionFactory.getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            UserRepository userRepository = new UserRepository();
            User user = userRepository.get(userId).get();
            Assertions.assertEquals(newLoginPass, user.getLogin());
            Assertions.assertEquals(newLoginPass, user.getPassword());
            transaction.commit();
        }
    }

    @AfterAll
    public static void tearDown() {
        destroy();
    }
}
