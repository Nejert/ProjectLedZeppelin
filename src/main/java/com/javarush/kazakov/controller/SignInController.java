package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.SessionFactory;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.config.constants.Param;
import com.javarush.kazakov.dto.user.UserTo;
import com.javarush.kazakov.service.UserService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@WebServlet(Loc.SIGN_IN)
public class SignInController extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.trace("Initializing Servlet");
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("Forwarding to '{}'", LocJSP.SIGN_IN);
        req.getRequestDispatcher(LocJSP.SIGN_IN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter(Param.LOGIN);
        log.trace("Returned parameter: '{}', value: '{}'", Param.LOGIN, login);
        String password = req.getParameter(Param.PASSWORD);
        log.trace("Returned parameter: '{}', value: '{}'", Param.LOGIN, login);
        UserService userService = new UserService();
        Optional<UserTo> userOpt =
                SessionFactory.executeInTransaction(() -> userService.get(login, password));
        if (userOpt.isPresent() && userOpt.get().password().equals(password)) {
            UserTo user = userOpt.get();
            log.trace("User's password is correct");
            log.trace("Setting session attribute '{}' to '{}'", Attr.USER, user);
            req.getSession().setAttribute(Attr.USER, user);
            log.trace("Redirecting to '/' page");
            resp.sendRedirect("/");
        } else {
            log.warn("User's login or password is incorrect");
            String message = "Wrong login or password";
            log.trace("Setting session attribute '{}' to '{}'", Attr.ERROR_MESSAGE, message);
            req.getSession().setAttribute(Attr.ERROR_MESSAGE, message);
            log.trace("Redirecting to '{}' page", Loc.SIGN_IN);
            resp.sendRedirect(Loc.SIGN_IN);
        }
    }
}
