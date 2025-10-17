package com.javarush.kazakov.dto.user;


import com.javarush.kazakov.entity.user.Role;

public record UserTo(
        Integer id,
        String login,
        String password,
        Role role,
        Integer victory,
        Integer defeat,
        String image
) {
}

