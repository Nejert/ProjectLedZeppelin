package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.SessionFactory;
import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.config.constants.Param;
import com.javarush.kazakov.dto.user.UserTo;
import com.javarush.kazakov.entity.user.Role;
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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 50    // 50 MB
)
@WebServlet(Loc.SIGN_UP)
public class SignUpController extends HttpServlet {
    ImageService imageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.trace("Initializing Servlet");
        super.init(config);
        imageService = Winter.find(ImageService.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("Forwarding to '{}'", LocJSP.SIGN_UP);
        req.getRequestDispatcher(LocJSP.SIGN_UP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part imagePart = req.getPart(Param.IMAGE_FILE);
        String login = req.getParameter(Param.LOGIN);
        log.trace("Returned parameter: '{}', value: '{}'", Param.LOGIN, login);
        String password = req.getParameter(Param.PASSWORD);
        log.trace("Returned parameter: '{}', value: '{}'", Param.PASSWORD, password);
        String imageName = imageService.loadImage(login, imagePart);
        log.trace("New user image filename: '{}'", imageName);
        UserTo newUser = new UserTo(
                null,
                login,
                password,
                Role.USER,
                0,
                0,
                imageName
        );
        UserService userService = new UserService();
        UserTo user = SessionFactory.executeInTransaction(() -> {
            userService.create(newUser);
            return userService.get(login, password).orElseThrow();
        });
        log.trace("Setting session attribute '{}' to '{}'", Attr.USER, user);
        req.getSession().setAttribute(Attr.USER, user);
        log.trace("Redirecting to '/' page");
        resp.sendRedirect("/");
    }
}
