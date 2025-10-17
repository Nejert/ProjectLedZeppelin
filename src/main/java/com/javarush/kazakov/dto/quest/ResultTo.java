package com.javarush.kazakov.dto.quest;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record ResultTo(
        @JsonIgnore
        Integer id,
        String title,
        Boolean victory
) {
}
