package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Answer implements QuestEntity {
    private final String text;
    private final Question nextQuestion;
    private final Result endResult;
}
