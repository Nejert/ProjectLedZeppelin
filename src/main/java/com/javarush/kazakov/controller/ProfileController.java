package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.config.constants.Param;
import com.javarush.kazakov.entity.User;
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
@WebServlet(Loc.PROFILE)
public class ProfileController extends HttpServlet {
    private User user;
    ImageService imageService;
    UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.trace("Initializing Servlet");
        super.init(config);
        imageService = Winter.find(ImageService.class);
        userService = Winter.find(UserService.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("Getting session attribute: '{}'", Attr.USER);
        Object userObj = req.getSession().getAttribute(Attr.USER);
        if (userObj != null) {
            user = (User) userObj;
            log.trace("'{}' is '{}'", Attr.USER, user);
            log.trace("Forwarding to '{}'", LocJSP.PROFILE);
            req.getRequestDispatcher(LocJSP.PROFILE).forward(req, resp);
        } else {
            log.trace("'{}' is null", Attr.USER);
            log.trace("Redirecting to '{}' page", Loc.SIGN_IN);
            resp.sendRedirect(Loc.SIGN_IN);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String change = req.getParameter(Param.CHANGE);
        log.trace("Returned parameter: '{}', value: '{}'", Param.CHANGE, change);
        if (change != null) {
            switch (change) {
                case Param.LOGIN -> changeLogin(req);
                case Param.PASSWORD -> changePassword(req);
                case Param.DELETE -> deleteUser(req);
            }
        }
        if (req.getContentType().contains("multipart/form-data")) {
            log.trace("Content type: '{}'", req.getContentType());
            log.trace("Getting request part '{}'", Param.CHANGE_IMAGE);
            Part changeImage = req.getPart(Param.CHANGE_IMAGE);
            log.trace("Returned '{}'", changeImage);
            if (changeImage != null) changeAvatar(req, changeImage);
        }
        log.trace("Redirecting to '{}' page", Loc.PROFILE);
        resp.sendRedirect(Loc.PROFILE);
    }

    private void changeAvatar(HttpServletRequest req, Part imagePart) {
        log.trace("Changing '{}' avatar", user.getLogin());
        String imageName = imageService.loadImage(user.getLogin(), imagePart);
        User newUser = User.builder()
                .login(user.getLogin())
                .password(user.getPassword())
                .role(user.getRole())
                .victory(user.getVictory())
                .defeat(user.getDefeat())
                .image(imageName)
                .build();
        userService.update(newUser);
        User repoUpdatedUser = userService.get(newUser.getLogin());
        log.trace("Setting session attribute '{}' to '{}'", Attr.USER, repoUpdatedUser);
        req.getSession().setAttribute(Attr.USER, repoUpdatedUser);
    }

    private void changeLogin(HttpServletRequest req) {
        log.trace("Changing '{}' login", user.getLogin());
        String newLogin = req.getParameter(Param.NEW_LOGIN);
        log.trace("Returned parameter: '{}', value: '{}'", Param.NEW_LOGIN, newLogin);
        User newUser = User.builder()
                .login(newLogin)
                .password(user.getPassword())
                .role(user.getRole())
                .victory(user.getVictory())
                .defeat(user.getDefeat())
                .image(user.getImage())
                .build();
        userService.update(newUser);
        User repoUpdatedUser = userService.get(newUser.getLogin());
        log.trace("Setting session attribute '{}' to '{}'", Attr.USER, repoUpdatedUser);
        req.getSession().setAttribute(Attr.USER, repoUpdatedUser);
    }

    private void changePassword(HttpServletRequest req) {
        log.trace("Changing '{}' password", user.getLogin());
        String password = req.getParameter(Param.PASSWORD);
        log.trace("Returned parameter: '{}', value: '{}'", Param.PASSWORD, password);
        String newPassword = req.getParameter(Param.NEW_PASSWORD);
        log.trace("Returned parameter: '{}', value: '{}'", Param.NEW_PASSWORD, newPassword);
        if (password.equals(user.getPassword())) {
            log.trace("User's password is correct");
            User newUser = User.builder()
                    .login(user.getLogin())
                    .password(newPassword)
                    .role(user.getRole())
                    .victory(user.getVictory())
                    .defeat(user.getDefeat())
                    .image(user.getImage())
                    .build();
            userService.update(newUser);
            User repoUpdatedUser = userService.get(newUser.getLogin());
            log.trace("Setting session attribute '{}' to '{}'", Attr.USER, repoUpdatedUser);
            req.getSession().setAttribute(Attr.USER, repoUpdatedUser);
        } else {
            String message = "Passwords do not match!";
            log.warn(message);
            log.trace("Setting session attribute '{}' to '{}'", Attr.ERROR_MESSAGE, message);
            req.getSession().setAttribute(Attr.ERROR_MESSAGE, message);
        }
    }

    private void deleteUser(HttpServletRequest req) {
        log.trace("Deleting user '{}'", user.getLogin());
        if (req.getParameter(Param.YES) != null) {
            log.trace("Parameter '{}' is not null", Param.YES);
            userService.delete(user);
            log.trace("Setting session attribute '{}' to null", Attr.USER);
            req.getSession().setAttribute(Attr.USER, null);
        } else if (req.getParameter(Param.NO) != null){
            log.trace("Parameter '{}' is not null, aborting deletion", Param.NO);
        }
    }
}
