package com.javarush.kazakov;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.entity.UserRole;
import com.javarush.kazakov.repository.DB;
import com.javarush.kazakov.service.UserService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {
    UserService userService;

    @BeforeEach
    public void setUp() {
        DB.getInstance().restoreDefaultDBFile();
        userService = Winter.find(UserService.class);
    }

    @Test
    public void gettingUserTest() {
        //Given
        String username = "admin";
        //When
        User admin = userService.get(username);
        //Then
        assertEquals("admin", admin.getLogin());
        assertEquals("admin", admin.getPassword());
        assertEquals(UserRole.ADMIN, admin.getRole());
        assertEquals(0, admin.getVictory());
        assertEquals(0, admin.getDefeat());
        assertEquals(1, admin.getQuestQuantity());
    }

    @Test
    public void updatingUserLoginTest() {
        //Given
        String username = "admin";
        String newUsername = "SuperAdmin777";
        //When
        User admin = userService.get(username);
        User newAdmin = User.builder()
                .login(newUsername)
                .password(admin.getPassword())
                .role(admin.getRole())
                .victory(admin.getVictory())
                .defeat(admin.getDefeat())
                .image(admin.getImage())
                .questQuantity(admin.getQuestQuantity())
                .build();
        userService.update(newAdmin);
        admin = userService.get(newUsername);
        //Then
        assertEquals(newUsername, admin.getLogin());
        assertEquals("admin", admin.getPassword());
        assertEquals(UserRole.ADMIN, admin.getRole());
        assertEquals(0, admin.getVictory());
        assertEquals(0, admin.getDefeat());
        assertEquals(1, admin.getQuestQuantity());
    }

    @Test
    public void updatingUserPasswordTest() {
        //Given
        String username = "admin";
        String newPassword = "SuperAdmin777";
        //When
        User admin = userService.get(username);
        User newAdmin = User.builder()
                .login(admin.getLogin())
                .password(newPassword)
                .role(admin.getRole())
                .victory(admin.getVictory())
                .defeat(admin.getDefeat())
                .image(admin.getImage())
                .questQuantity(admin.getQuestQuantity())
                .build();
        userService.update(newAdmin);
        admin = userService.get(username);
        //Then
        assertEquals("admin", admin.getLogin());
        assertEquals(newPassword, admin.getPassword());
        assertEquals(UserRole.ADMIN, admin.getRole());
        assertEquals(0, admin.getVictory());
        assertEquals(0, admin.getDefeat());
        assertEquals(1, admin.getQuestQuantity());
    }

    @Test
    public void creatingUserTest() {
        //Given
        String username = "ManEatsPants";
        String password = "LOWERCASE777";
        //When
        User user = User.builder()
                .login(username)
                .password(password)
                .role(UserRole.USER)
                .victory(0)
                .defeat(0)
                .image("no-image.png")
                .questQuantity(0)
                .build();
        userService.create(user);
        User dbUser = userService.get(username);
        //Then
        assertEquals(username, dbUser.getLogin());
        assertEquals(password, dbUser.getPassword());
        assertEquals(UserRole.USER, dbUser.getRole());
        assertEquals(0, dbUser.getVictory());
        assertEquals(0, dbUser.getDefeat());
        assertEquals(0, dbUser.getQuestQuantity());
    }

    @Test
    public void deletingUserTest() {
        //Given
        String username = "user";
        //When
        userService.delete(userService.get(username));
        //Then
        User user = userService.get(username);
        assertNull(user);
    }

    @Test
    public void gettingAllUsersTest() {
        //Given
        //When
        List<User> all = userService.getAll();
        List<String> logins = new ArrayList<>();
        all.forEach(e -> logins.add(e.getLogin()));
        //Then
        assertEquals(2, all.size());
        assertIterableEquals(logins, List.of("admin", "user"));
    }

    @Test
    public void countCreatedQuestsTest() {
        //Given
        User admin = User.builder().login("admin").build();
        //When
        int quantity = userService.countCreatedQuests(admin);
        //Then
        assertEquals(1, quantity);
    }
}
