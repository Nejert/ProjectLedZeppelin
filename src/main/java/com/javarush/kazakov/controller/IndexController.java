package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.LocJSP;
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
        log.trace("Initializing Servlet");
        super.init(config);
        questService = Winter.find(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        Map<String, String> questAuthorMap = questService.getQuestAuthorMap();
        log.trace("Setting session attribute '{}' to '{}'", Attr.QUEST_AUTHOR_MAP, questAuthorMap);
        req.getSession().setAttribute(Attr.QUEST_AUTHOR_MAP, questAuthorMap);
        log.trace("Forwarding to '{}'", LocJSP.INDEX);
        req.getRequestDispatcher(LocJSP.INDEX).forward(req, resp);
    }
}
