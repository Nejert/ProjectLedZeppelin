package com.javarush.kazakov.dto.quest;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javarush.kazakov.dto.user.UserTo;

public record QuestTo (
        @JsonIgnore
        Integer id,
        String title,
        QuestionTo currentQuestion,
        @JsonIgnore
        UserTo author
) {
}
