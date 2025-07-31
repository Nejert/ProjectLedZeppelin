package com.javarush.kazakov.repository;

import lombok.Getter;

@Getter
public enum Table {
    ANSWER_NEXT_QUESTION(new String[]{"ANSWER_ID", "QUESTION_ID"}),
    ANSWER_RESULT(new String[]{"ANSWER_ID", "RESULT_ID"}),
    QUEST_FIRST_QUESTION(new String[]{"QUEST_ID", "QUESTION_ID"}),
    QUESTION_ANSWER(new String[]{"QUESTION_ID", "ANSWER_ID"});

    private final String[] columns;

    Table(String[] columns) {
        this.columns = columns;
    }
}
