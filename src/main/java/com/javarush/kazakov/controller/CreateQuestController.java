package com.javarush.kazakov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.service.QuestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet("/create-quest")
public class CreateQuestController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect("/sign-in");
        } else {
            req.getRequestDispatcher("WEB-INF/create-quest.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        ObjectMapper objectMapper = new ObjectMapper();
        try (ServletInputStream questStream = req.getInputStream()) {
            Quest quest = objectMapper.readValue(questStream, Quest.class);
            QuestService questService = new QuestService();
            questService.create(user, quest);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        resp.sendRedirect("/profile");
    }
}
