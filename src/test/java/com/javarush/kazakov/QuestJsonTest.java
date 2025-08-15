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
        String jru = """
                {
                  "questName" : "JavaRush Quest",
                  "currentQuestion" : {
                    "text" : "Ты потерял память. Принять вызов НЛО?",
                    "answers" : [ {
                      "text" : "Отклонить вызов",
                      "nextQuestion" : null,
                      "endResult" : {
                        "text" : "Ты отклонил вызов. Поражение",
                        "victory" : false
                      }
                    }, {
                      "text" : "Принять вызов",
                      "nextQuestion" : {
                        "text" : "Ты принял вызов. Поднимаешься на мостик к капитану?",
                        "answers" : [ {
                          "text" : "Отказаться подниматься на мостик",
                          "nextQuestion" : null,
                          "endResult" : {
                            "text" : "Ты не пошел на переговоры. Поражение",
                            "victory" : false
                          }
                        }, {
                          "text" : "Подняться на мостик",
                          "nextQuestion" : {
                            "text" : "Ты поднялся на мостик. Ты кто?",
                            "answers" : [ {
                              "text" : "Солгать о себе",
                              "nextQuestion" : null,
                              "endResult" : {
                                "text" : "Твою ложь разоблачили. Поражение",
                                "victory" : false
                              }
                            }, {
                              "text" : "Рассказать правду о себе",
                              "nextQuestion" : null,
                              "endResult" : {
                                "text" : "Тебя вернули домой. Победа",
                                "victory" : true
                              }
                            } ]
                          },
                          "endResult" : null
                        } ]
                      },
                      "endResult" : null
                    } ]
                  }
                }
                """;
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
