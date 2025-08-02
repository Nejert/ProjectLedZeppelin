package com.javarush.kazakov.entity;

import lombok.Getter;

@Getter
public class User {
    private final String login;
    private final String password;
    private final UserRole role;
    private final int victory;
    private final int defeat;

    public User(String login, String password, UserRole role, int victory, int defeat) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.victory = victory;
        this.defeat = defeat;
    }
}
