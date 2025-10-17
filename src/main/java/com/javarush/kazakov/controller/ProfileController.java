package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.SessionFactory;
import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.config.constants.Param;
import com.javarush.kazakov.dto.user.UserTo;
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

    private ImageService imageService;

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
        log.trace("Getting session attribute: '{}'", Attr.USER);
        Object userObj = req.getSession().getAttribute(Attr.USER);
        if (userObj != null) {
            UserTo user = (UserTo) userObj;
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
        UserTo user = (UserTo) req.getSession().getAttribute(Attr.USER);
        log.trace("Returned parameter: '{}', value: '{}'", Param.CHANGE, change);
        if (change != null) {
            switch (change) {
                case Param.LOGIN -> changeLogin(req, user);
                case Param.PASSWORD -> changePassword(req, user);
                case Param.DELETE -> deleteUser(req, user);
            }
        }
        if (req.getContentType().contains("multipart/form-data")) {
            log.trace("Content type: '{}'", req.getContentType());
            log.trace("Getting request part '{}'", Param.CHANGE_IMAGE);
            Part changeImage = req.getPart(Param.CHANGE_IMAGE);
            log.trace("Returned '{}'", changeImage);
            if (changeImage != null) changeAvatar(req, changeImage, user);
        }
        log.trace("Redirecting to '{}' page", Loc.PROFILE);
        resp.sendRedirect(Loc.PROFILE);
    }

    private void changeAvatar(HttpServletRequest req, Part imagePart, UserTo user) {
        log.trace("Changing '{}' avatar", user.login());
        String imageName = imageService.loadImage(user.login(), imagePart);
        UserTo newUser = new UserTo(
                user.id(),
                user.login(),
                user.password(),
                user.role(),
                user.victory(),
                user.defeat(),
                imageName
        );
        updateUser(req, newUser, newUser);
    }

    private void changeLogin(HttpServletRequest req, UserTo user) {
        log.trace("Changing '{}' login", user.login());
        String newLogin = req.getParameter(Param.NEW_LOGIN);
        log.trace("Returned parameter: '{}', value: '{}'", Param.NEW_LOGIN, newLogin);
        UserTo newUser = new UserTo(
                user.id(),
                newLogin,
                user.password(),
                user.role(),
                user.victory(),
                user.defeat(),
                user.image()
        );
        updateUser(req, user, newUser);
    }

    private void changePassword(HttpServletRequest req, UserTo user) {
        log.trace("Changing '{}' password", user.login());
        String password = req.getParameter(Param.PASSWORD);
        log.trace("Returned parameter: '{}', value: '{}'", Param.PASSWORD, password);
        String newPassword = req.getParameter(Param.NEW_PASSWORD);
        log.trace("Returned parameter: '{}', value: '{}'", Param.NEW_PASSWORD, newPassword);
        if (password.equals(user.password())) {
            log.trace("User's password is correct");
            UserTo newUser = new UserTo(
                    user.id(),
                    user.login(),
                    newPassword,
                    user.role(),
                    user.victory(),
                    user.defeat(),
                    user.image()
            );
            updateUser(req, user, newUser);
        } else {
            String message = "Passwords do not match!";
            log.warn(message);
            log.trace("Setting session attribute '{}' to '{}'", Attr.ERROR_MESSAGE, message);
            req.getSession().setAttribute(Attr.ERROR_MESSAGE, message);
        }
    }

    private void updateUser(HttpServletRequest req, UserTo user, UserTo newUser) {
        UserService userService = new UserService();
        UserTo updatedUser = SessionFactory.executeInTransaction(() -> {
            userService.update(newUser);
            return userService.get(user.id()).orElseThrow();
        });
        log.trace("Setting session attribute '{}' to '{}'", Attr.USER, updatedUser);
        req.getSession().setAttribute(Attr.USER, updatedUser);
    }

    private void deleteUser(HttpServletRequest req, UserTo user) {
        log.trace("Deleting user '{}'", user.login());
        if (req.getParameter(Param.YES) != null) {
            log.trace("Parameter '{}' is not null", Param.YES);
            UserService userService = new UserService();
            SessionFactory.executeInTransaction(() -> userService.delete(user));
            log.trace("Setting session attribute '{}' to null", Attr.USER);
            req.getSession().setAttribute(Attr.USER, null);
        } else if (req.getParameter(Param.NO) != null) {
            log.trace("Parameter '{}' is not null, aborting deletion", Param.NO);
        }
    }
}
