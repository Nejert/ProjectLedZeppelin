package com.javarush.kazakov.controller;

import com.javarush.kazakov.entity.Answer;
import com.javarush.kazakov.entity.Quest;
import com.javarush.kazakov.service.QuestService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/quest/*")
public class QuestController extends HttpServlet {
    private QuestService questService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        questService = new QuestService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        int lastSlashIndex = url.lastIndexOf('/');
        String lastPathSegment = url.substring(lastSlashIndex + 1);
        if ("quest".equals(lastPathSegment)) {
            List<String> questsList = questService.getQuestsList();
            req.getSession().setAttribute("questsList", questsList);
            req.getSession().setAttribute("currentQuest", null);
            req.getRequestDispatcher("/WEB-INF/quests-list.jsp").forward(req, resp);
        } else {
            if (req.getSession().getAttribute("currentQuest") == null) {
                Quest quest = questService.get(lastPathSegment.replaceAll("-", " "));
                req.getSession().setAttribute("currentQuest", quest);
            }
            req.getRequestDispatcher("/WEB-INF/quest.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Quest quest = (Quest) req.getSession().getAttribute("currentQuest");
        int answerIdx = Integer.parseInt(req.getParameter("answer"));
        Answer answer = quest.getCurrentQuestion().getAnswers().get(answerIdx);
        if (answer.getNextQuestion() != null){
            quest.setCurrentQuestion(answer.getNextQuestion());
            resp.sendRedirect(req.getRequestURI());
        } else if (answer.getEndResult() != null){
            req.getSession().setAttribute("result",answer.getEndResult());
            resp.sendRedirect("/result");
        }
    }
}
