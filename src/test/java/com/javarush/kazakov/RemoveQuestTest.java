package com.javarush.kazakov;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.repository.DB;
import com.javarush.kazakov.service.QuestService;
import com.javarush.kazakov.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;


public class RemoveQuestTest {

    @Test
    public void checkTestQuestExists() {
        DB.getInstance().restoreDefaultDBFile();
        User admin = Winter.find(UserService.class).get("admin");
        QuestService questService = Winter.find(QuestService.class);
        questService.create(admin, TestQuest.getTestQuest());
        questService.delete(TestQuest.getTestQuest());
        assertNull(questService.get(TestQuest.getTestQuest().getQuestName()));
    }

}
