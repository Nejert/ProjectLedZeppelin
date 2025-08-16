package com.javarush.kazakov;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.repository.QuestReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class QuestJsonTest {
    public static Quest quest;

    @BeforeAll
    public static void setUp() {
        quest = new QuestReader().read("JavaRush Quest");
    }

    @Test
    public void test() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String jsonString = objectMapper.writeValueAsString(quest);
            log.debug(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
