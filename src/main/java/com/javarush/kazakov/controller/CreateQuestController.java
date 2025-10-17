package com.javarush.kazakov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.kazakov.config.SessionFactory;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.dto.quest.QuestTo;
import com.javarush.kazakov.dto.user.UserTo;
import com.javarush.kazakov.exception.QuestException;
import com.javarush.kazakov.service.QuestService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet(Loc.CREATE_QUEST)
public class CreateQuestController extends HttpServlet {
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.trace("Initializing Servlet");
        super.init(config);
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("Getting session attribute: '{}'", Attr.USER);
        if (req.getSession().getAttribute(Attr.USER) == null) {
            log.trace("Attribute '{}' is null", Attr.USER);
            log.trace("Redirecting to '{}' page", Loc.SIGN_IN);
            resp.sendRedirect(Loc.SIGN_IN);
        } else {
            log.trace("Attribute '{}' is not null", Attr.USER);
            log.trace("Forwarding to '{}'", LocJSP.CREATE_QUEST);
            req.getRequestDispatcher(LocJSP.CREATE_QUEST).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.trace("Getting session attribute: '{}'", Attr.USER);
        UserTo user = (UserTo) req.getSession().getAttribute(Attr.USER);
        log.trace("'{}' is '{}'", Attr.USER, user);
        try (ServletInputStream questStream = req.getInputStream()) {
            log.trace("Content type: 'application/json'");
            log.trace("Object mapper reads request body");
            QuestTo quest = objectMapper.readValue(questStream, QuestTo.class);
            log.trace("Returned quest: {}", quest);
            QuestService questService = new QuestService();
            SessionFactory.executeInTransaction(() -> questService.create(quest, user));
        } catch (IOException e) {
            throw new QuestException("Unable to read quest from request", e);
        }
        log.trace("Redirecting to '{}' page", Loc.RESULT);
        resp.sendRedirect(Loc.PROFILE);
    }
}
