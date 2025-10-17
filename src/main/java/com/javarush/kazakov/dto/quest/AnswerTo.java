package com.javarush.kazakov.dto.quest;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AnswerTo(
        @JsonIgnore
        Integer id,
        String title,
        QuestionTo question,
        ResultTo result
) {
}
