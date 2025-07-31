package com.javarush.kazakov.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Quest implements QuestEntity {
    private final String questName;
    @Setter
    private Question currentQuestion;

    public Quest(String questName, Question currentQuestion) {
        this.questName = questName;
        this.currentQuestion = currentQuestion;
    }

    @Override
    public String getText() {
        return questName;
    }
}
