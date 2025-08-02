package com.javarush.kazakov;

import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.entity.UserRole;
import com.javarush.kazakov.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserServiceTest {
    @Test
    public void gettingUserFromDBTest() {
        UserService userService = new UserService();
        User admin = userService.get("Admin");
        Assertions.assertEquals("Admin", admin.getLogin());
        Assertions.assertEquals("admin", admin.getPassword());
        Assertions.assertEquals(UserRole.ADMIN, admin.getRole());
        Assertions.assertEquals(0, admin.getVictory());
        Assertions.assertEquals(0, admin.getDefeat());
    }
}
