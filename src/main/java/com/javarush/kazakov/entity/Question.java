package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Question implements QuestEntity {
    private String text;
    private List<Answer> answers;
}
