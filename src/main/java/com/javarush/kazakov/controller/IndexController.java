package com.javarush.kazakov.controller;

import com.javarush.kazakov.service.QuestService;
import com.javarush.kazakov.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
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
        //autologin(req);
        log.info("doGet: {}", req.getRequestURI());
        Map<String, String> questAuthorMap = questService.getQuestAuthorMap();
        req.getSession().setAttribute("questAuthorMap", questAuthorMap);
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }

    private void autologin(HttpServletRequest req){
        req.getSession().setAttribute("user", new UserService().get("Admin"));
    }
}
