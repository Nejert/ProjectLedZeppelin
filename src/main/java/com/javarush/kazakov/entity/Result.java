package com.javarush.kazakov.entity;

import lombok.Getter;

@Getter
public class Result implements QuestEntity {
    private final String text;

    public Result(String text) {
        this.text = text;
    }
}
