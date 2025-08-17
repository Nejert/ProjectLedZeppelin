package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.entity.Answer;
import com.javarush.kazakov.entity.Quest;
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
@WebServlet("/quest/*")
public class QuestController extends HttpServlet {
    private QuestService questService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        questService = Winter.find(QuestService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") == null) {
            resp.sendRedirect("/sign-in");
            return;
        }
        if (req.getSession().getAttribute("quest") != null){
            req.getRequestDispatcher("/WEB-INF/quest.jsp").forward(req, resp);
            return;
        }
        String[] pathItems = req.getRequestURI().split("/");
        String path = pathItems[pathItems.length - 1];
        log.debug(path);
        if (path.equals("quest")) {
            req.getRequestDispatcher("/").forward(req, resp);
        } else {
            String questName = req.getPathInfo().replaceAll("[/-]", " ").trim();
            Quest quest = questService.get(questName);
            if (quest == null) {
                String message = "Quest '%s' not found".formatted(questName);
                log.warn(message);
                req.getSession().setAttribute("errorMessage", message);
                req.getRequestDispatcher("/").forward(req, resp);
                return;
            }
            req.getSession().setAttribute("quest", quest);
            req.getRequestDispatcher("/WEB-INF/quest.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Quest quest = (Quest) req.getSession().getAttribute("quest");
        int answerIdx = Integer.parseInt(req.getParameter("answer"));
        Answer answer = quest.getCurrentQuestion().getAnswers().get(answerIdx);
        if (answer.getNextQuestion() != null) {
            quest.setCurrentQuestion(answer.getNextQuestion());
            req.getSession().setAttribute("quest", quest);
            resp.sendRedirect(req.getRequestURI());
        } else if (answer.getEndResult() != null) {
            req.getSession().setAttribute("result", answer.getEndResult());
            resp.sendRedirect("/result");
        }
    }
}
