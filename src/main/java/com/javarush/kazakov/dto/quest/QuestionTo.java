package com.javarush.kazakov.dto.quest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record QuestionTo(
        @JsonIgnore
        Integer id,
        String title,
        List<AnswerTo> answers
) {
}

