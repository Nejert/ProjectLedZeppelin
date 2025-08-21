package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quest implements QuestEntity {
    private String questName;
    private Question currentQuestion;

    public void setCurrentQuestion(Question currentQuestion) {
        log.trace("Setting current question: {}", currentQuestion);
        this.currentQuestion = currentQuestion;
    }

    @Override
    public String getText() {
        return questName;
    }
}
