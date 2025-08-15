package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements QuestEntity {
    private String text;
    private Question nextQuestion;
    private Result endResult;
}
