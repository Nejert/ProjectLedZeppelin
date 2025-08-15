package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Result implements QuestEntity {
    private String text;
    private boolean victory;
}
