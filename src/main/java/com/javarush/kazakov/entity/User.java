package com.javarush.kazakov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private final String login;
    private final String password;
    private final UserRole role;
    private final int victory;
    private final int defeat;
    private final String image;
    private final int questQuantity;
}
