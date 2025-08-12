package com.javarush.kazakov.controller;

import com.javarush.kazakov.entity.Result;
import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/result")
public class ResultController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object resultObj = req.getSession().getAttribute("result");
        boolean isVictory = false;
        if (resultObj != null) {
            isVictory = ((Result) resultObj).isVictory();
        }
        Object userObj = req.getSession().getAttribute("user");
        User user = null;
        if (userObj != null) {
            user = (User) userObj;
            User userUpdated = User.builder()
                    .login(user.getLogin())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .victory(isVictory ? user.getVictory() + 1 : user.getVictory())
                    .defeat(isVictory ? user.getDefeat() : user.getDefeat() + 1)
                    .image(user.getImage())
                    .questQuantity(user.getQuestQuantity())
                    .build();
            UserService userService = new UserService();
            userService.update(userUpdated);
            req.getSession().setAttribute("user", userService.get(userUpdated.getLogin()));
        }
        req.getSession().setAttribute("quest", null);
        req.getRequestDispatcher("/WEB-INF/result.jsp").forward(req, resp);
    }
}
