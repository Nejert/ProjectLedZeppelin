package com.javarush.kazakov.entity;

import lombok.Getter;

import java.util.List;

@Getter
public class Question implements QuestEntity {
    private final String text;
    private final List<Answer> answers;

    public Question(String text, List<Answer> answers) {
        this.text = text;
        this.answers = answers;
    }
}
