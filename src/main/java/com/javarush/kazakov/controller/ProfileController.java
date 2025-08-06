package com.javarush.kazakov.controller;

import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
    private User user;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object userObj = req.getSession().getAttribute("user");
        if (userObj != null) {
            user = (User) userObj;
            req.getRequestDispatcher("/WEB-INF/profile.jsp").forward(req, resp);
        } else {
            resp.sendRedirect("/sign-in");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String change = req.getParameter("change");
        switch (change) {
            case "login" -> changeLogin(req);
            case "password" -> changePassword(req);
            case "delete" -> deleteUser(req);
        }
        resp.sendRedirect("/profile");
    }

    private void changeLogin(HttpServletRequest req) {
        User newUser = new User(
                req.getParameter("newLogin"),
                user.getPassword(),
                user.getRole(),
                user.getVictory(),
                user.getDefeat());
        UserService userService = new UserService();
        userService.update(newUser);
        req.getSession().setAttribute("user", userService.get(newUser.getLogin()));
    }

    private void changePassword(HttpServletRequest req) {
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        if (oldPassword.equals(user.getPassword())) {
            User newUser = new User(
                    user.getLogin(),
                    newPassword,
                    user.getRole(),
                    user.getVictory(),
                    user.getDefeat());
            UserService userService = new UserService();
            userService.update(newUser);
            req.getSession().setAttribute("user", userService.get(newUser.getLogin()));
        }
    }

    private void deleteUser(HttpServletRequest req) {
        if (req.getParameter("yes") != null) {
            UserService userService = new UserService();
            userService.delete(user);
            req.getSession().setAttribute("user", null);
        }
    }
}
