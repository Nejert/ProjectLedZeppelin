package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.entity.User;
import com.javarush.kazakov.entity.UserRole;
import com.javarush.kazakov.service.ImageService;
import com.javarush.kazakov.service.UserService;
import jakarta.servlet.ServletConfig;
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
@WebServlet("/sign-up")
public class SignUpController extends HttpServlet {
    ImageService imageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        imageService = Winter.find(ImageService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part imagePart = req.getPart("imageFile");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String imageName = imageService.loadImage(login, imagePart);
        User user = User.builder()
                .login(login)
                .password(password)
                .role(UserRole.USER)
                .victory(0)
                .defeat(0)
                .image(imageName)
                .build();
        UserService userService = new UserService();
        userService.create(user);
        req.getSession().setAttribute("user", user);
        resp.sendRedirect("/");
    }
}
