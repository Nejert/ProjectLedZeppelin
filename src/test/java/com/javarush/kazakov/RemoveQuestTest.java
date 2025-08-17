package com.javarush.kazakov;

import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.repository.QuestReader;
import com.javarush.kazakov.repository.QuestRemover;
import com.javarush.kazakov.repository.QuestWriter;
import com.javarush.kazakov.service.UserService;
import org.junit.jupiter.api.Test;

import static com.javarush.kazakov.TestQuest.QUEST_NAME;
import static org.junit.jupiter.api.Assertions.assertNull;


public class RemoveQuestTest {

    @Test
    public void checkTestQuestExists() {
        QuestWriter questWriter = new QuestWriter();
        User admin = new UserService().get("Admin");
        try {
            questWriter.write(admin, TestQuest.getTestQuest());
        } catch (QuestException e) {
            System.out.println(e.getMessage());
        }

        QuestRemover questRemover = new QuestRemover();
        questRemover.remove(QUEST_NAME);

        QuestReader questReader = new QuestReader();
        assertNull(questReader.read(QUEST_NAME));
    }

}
