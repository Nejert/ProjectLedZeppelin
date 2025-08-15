package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quest implements QuestEntity {
    private String questName;
    @Setter
    private Question currentQuestion;

    @Override
    public String getText() {
        return questName;
    }
}
