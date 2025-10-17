package com.javarush.kazakov.object;

import com.javarush.kazakov.entity.user.Role;
import com.javarush.kazakov.entity.user.User;
import lombok.Getter;

@Getter
public enum TestUser {
    TEST(new User(null, "test", "test", Role.USER,0,0,"test.png"));

    private final User user;

    TestUser(User user) {
        this.user = user;
    }
}
