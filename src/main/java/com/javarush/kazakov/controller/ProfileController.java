package com.javarush.kazakov.controller;

import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.entity.UserRole;
import com.javarush.kazakov.service.ImageService;
import com.javarush.kazakov.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 50    // 50 MB
)
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
        if (change != null) {
            switch (change) {
                case "login" -> changeLogin(req);
                case "password" -> changePassword(req);
                case "delete" -> deleteUser(req);
            }
        }
        if (req.getContentType().contains("multipart/form-data")) {
            Part changeImage = req.getPart("changeImage");
            if (changeImage != null) changeAvatar(req, changeImage);
        }
        resp.sendRedirect("/profile");
    }

    private void changeAvatar(HttpServletRequest req, Part imagePart) throws ServletException, IOException {
        ImageService imageService = new ImageService();
        String imageName = imageService.loadImage(user.getLogin(), imagePart);
        User newUser = User.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .role(user.getRole())
                .victory(user.getVictory())
                .defeat(user.getDefeat())
                .image(imageName)
                .build();
        UserService userService = new UserService();
        userService.update(newUser);
        req.getSession().setAttribute("user", userService.get(newUser.getLogin()));
    }

    private void changeLogin(HttpServletRequest req) {
        User newUser = User.builder()
                .login(req.getParameter("newLogin"))
                .password(user.getPassword())
                .role(user.getRole())
                .victory(user.getVictory())
                .defeat(user.getDefeat())
                .image(user.getImage())
                .build();
        UserService userService = new UserService();
        userService.update(newUser);
        req.getSession().setAttribute("user", userService.get(newUser.getLogin()));
    }

    private void changePassword(HttpServletRequest req) {
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");
        if (oldPassword.equals(user.getPassword())) {
            User newUser = User.builder()
                    .login(user.getLogin())
                    .password(newPassword)
                    .role(user.getRole())
                    .victory(user.getVictory())
                    .defeat(user.getDefeat())
                    .image(user.getImage())
                    .build();
            UserService userService = new UserService();
            userService.update(newUser);
            req.getSession().setAttribute("user", userService.get(newUser.getLogin()));
        } else {
            String message = "Passwords do not match!";
            req.getSession().setAttribute("errorMessage", message);
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
