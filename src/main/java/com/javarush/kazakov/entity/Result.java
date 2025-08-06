package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Result implements QuestEntity {
    private final String text;
}
