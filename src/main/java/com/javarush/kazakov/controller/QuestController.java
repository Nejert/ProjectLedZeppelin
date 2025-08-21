package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.config.constants.Param;
import com.javarush.kazakov.entity.Answer;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.entity.Question;
import com.javarush.kazakov.entity.Result;
import com.javarush.kazakov.service.QuestService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet(Loc.QUEST + "/*")
public class QuestController extends HttpServlet {
    private QuestService questService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.trace("Initializing Servlet");
        super.init(config);
        questService = Winter.find(QuestService.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("Getting session attribute '{}'", Attr.USER);
        if (req.getSession().getAttribute(Attr.USER) == null) {
            log.trace("'{}' attribute is null", Attr.USER);
            log.trace("Redirecting to '{}' page", Loc.SIGN_IN);
            resp.sendRedirect(Loc.SIGN_IN);
            return;
        }
        log.trace("Getting session attribute '{}'", Attr.QUEST);
        if (req.getSession().getAttribute(Attr.QUEST) != null) {
            log.trace("'{}' attribute is not null", Attr.QUEST);
            log.trace("Forwarding to '{}'", LocJSP.QUEST);
            req.getRequestDispatcher(LocJSP.QUEST).forward(req, resp);
            return;
        } else {
            log.trace("'{}' attribute is null, continue looking for '{}'", Attr.QUEST, Attr.QUEST);
        }
        String[] pathItems = req.getRequestURI().split("/");
        String path = pathItems[pathItems.length - 1];
        log.debug("Last part of the request '{}'", path);
        if ("quest".equals(path)) {
            log.trace("Forwarding to '/'");
            req.getRequestDispatcher("/").forward(req, resp);
        } else {
            String questName = req.getPathInfo().replaceAll("[/-]", " ").trim();
            log.trace("Trying to get quest named as '{}'", questName);
            Quest quest = questService.get(questName);
            if (quest == null) {
                String message = "Quest '%s' not found".formatted(questName);
                log.warn(message);
                log.trace("Setting session attribute '{}' to '{}'", Attr.ERROR_MESSAGE, message);
                req.getSession().setAttribute(Attr.ERROR_MESSAGE, message);
                log.trace("Forwarding to '/'");
                req.getRequestDispatcher("/").forward(req, resp);
                return;
            }
            log.trace("Setting session attribute '{}' to '{}'", Attr.QUEST, quest);
            req.getSession().setAttribute(Attr.QUEST, quest);
            log.trace("Forwarding to '{}'", LocJSP.QUEST);
            req.getRequestDispatcher(LocJSP.QUEST).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.trace("Getting session attribute '{}'", Attr.QUEST);
        Quest quest = (Quest) req.getSession().getAttribute(Attr.QUEST);
        log.trace("Returned '{}'", quest);
        log.trace("Getting parameter '{}'", Param.ANSWER);
        int answerIdx = Integer.parseInt(req.getParameter(Param.ANSWER));
        log.trace("Returned parameter: '{}', value: '{}'", Param.ANSWER, answerIdx);
        log.trace("Getting answer by index '{}'", answerIdx);
        Answer answer = quest.getCurrentQuestion().getAnswers().get(answerIdx);
        log.trace("Returned '{}'", answer);
        if (answer.getNextQuestion() != null) {
            Question nextQuestion = answer.getNextQuestion();
            log.trace("Answer's 'nextQuestion' is not null: {}", nextQuestion);
            quest.setCurrentQuestion(nextQuestion);
            log.trace("Setting session attribute '{}' to '{}'", Attr.QUEST, quest);
            req.getSession().setAttribute(Attr.QUEST, quest);
            log.trace("Redirecting to '{}' page", req.getRequestURI());
            resp.sendRedirect(req.getRequestURI());
        } else if (answer.getEndResult() != null) {
            Result endResult = answer.getEndResult();
            log.trace("Answer's 'endResult' is not null: {}", endResult);
            log.trace("Setting session attribute '{}' to '{}'", Attr.RESULT, endResult);
            req.getSession().setAttribute(Attr.RESULT, endResult);
            log.trace("Redirecting to '{}' page", Loc.RESULT);
            resp.sendRedirect(Loc.RESULT);
        }
    }
}
