package com.javarush.kazakov;

import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.repository.QuestReader;
import com.javarush.kazakov.repository.QuestRemover;
import com.javarush.kazakov.repository.QuestWriter;
import org.junit.jupiter.api.Test;

import static com.javarush.kazakov.TestQuest.QUEST_NAME;
import static org.junit.jupiter.api.Assertions.assertNull;


public class RemoveQuestTest {

    @Test
    public void checkTestQuestExists() {
        QuestWriter questWriter = new QuestWriter(TestQuest.getTestQuest());
        try {
            questWriter.write();
        } catch (QuestException e) {
            System.out.println(e.getMessage());
        }

        QuestRemover questRemover = new QuestRemover(QUEST_NAME);
        questRemover.remove();

        QuestReader questReader = new QuestReader(QUEST_NAME);
        assertNull(questReader.read());
    }

}
