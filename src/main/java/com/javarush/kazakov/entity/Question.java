package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Question implements QuestEntity {
    private final String text;
    private final List<Answer> answers;
}
