package com.javarush.kazakov;

import com.javarush.kazakov.controller.CreateQuestController;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class CreateQuestControllerTest {

    @Test
    public void initTest() throws ServletException {
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        CreateQuestController createQuestController = new CreateQuestController();
        createQuestController.init(servletConfig);
        for (Field field : CreateQuestController.class.getDeclaredFields()) {
            field.setAccessible(true);
            Assertions.assertNotNull(field);
        }
    }
    @Test
    public void serviceTest() throws ServletException {
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        CreateQuestController createQuestController = new CreateQuestController();
        createQuestController.init(servletConfig);

    }
}
