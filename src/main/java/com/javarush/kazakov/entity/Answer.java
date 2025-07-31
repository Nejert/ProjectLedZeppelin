package com.javarush.kazakov.entity;

import lombok.Getter;

@Getter
public class Answer implements QuestEntity {
    private final String text;
    private final Question nextQuestion;
    private final Result endResult;

    public Answer(String text, Question nextQuestion, Result endResult) {
        this.text = text;
        this.nextQuestion = nextQuestion;
        this.endResult = endResult;
    }
}
