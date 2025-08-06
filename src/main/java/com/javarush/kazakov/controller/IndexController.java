package com.javarush.kazakov.controller;

import com.javarush.kazakov.service.QuestService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("")
public class IndexController extends HttpServlet {
    private QuestService questService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        questService = new QuestService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> questsList = questService.getQuestsList();
        req.getSession().setAttribute("questsList", questsList);
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }
}
