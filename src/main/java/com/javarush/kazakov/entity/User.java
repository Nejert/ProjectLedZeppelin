package com.javarush.kazakov.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record User(String login, String password, UserRole role, int victory, int defeat) {
}
