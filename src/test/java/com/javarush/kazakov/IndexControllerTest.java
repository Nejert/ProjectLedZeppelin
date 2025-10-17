package com.javarush.kazakov;

import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.controller.IndexController;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@Disabled
public class IndexControllerTest {

    @Test
    public void initTest() throws ServletException, NoSuchFieldException, IllegalAccessException {
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        IndexController indexController = new IndexController();
        indexController.init(servletConfig);
        Field questService = indexController.getClass().getDeclaredField("questService");
        questService.setAccessible(true);
        Assertions.assertNotNull(questService.get(indexController));
        Field userService = indexController.getClass().getDeclaredField("userService");
        userService.setAccessible(true);
        Assertions.assertNotNull(userService.get(indexController));
    }

    @Test
    public void doGetTest() throws ServletException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        //Given
        Map<String, Object> sessionAttributes = new HashMap<>();
        ServletConfig servletConfig = Mockito.mock(ServletConfig.class);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        IndexController indexController = Mockito.spy(IndexController.class);
        //When
        doAnswer(a -> sessionAttributes.get(a.getArgument(0)))
                .when(session)
                .getAttribute(anyString());
        doAnswer(a -> sessionAttributes.put(a.getArgument(0), a.getArgument(1)))
                .when(session)
                .setAttribute(anyString(), any());
        when(req.getSession()).thenReturn(session);
        when(req.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        //Then
        indexController.init(servletConfig);
        Method doGet = IndexController.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(indexController, req, resp);

        verify(req).getRequestDispatcher(LocJSP.INDEX);
        verify(requestDispatcher).forward(req, resp);

        LinkedHashMap<String, Object> questAuthorMap = (LinkedHashMap<String, Object>) req.getSession().getAttribute(Attr.QUEST_AUTHOR_MAP);
        Assertions.assertNotNull(questAuthorMap);
        Assertions.assertTrue(questAuthorMap.containsKey("JavaRush Quest"));
    }
}
