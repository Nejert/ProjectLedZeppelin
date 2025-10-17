package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.SessionFactory;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.dto.quest.ResultTo;
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

@Slf4j
@WebServlet(Loc.RESULT)
public class ResultController extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.trace("Initializing Servlet");
        super.init(config);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        log.trace("Getting session attribute: '{}'", Attr.RESULT);
        Object resultObj = req.getSession().getAttribute(Attr.RESULT);
        boolean isVictory = false;
        if (resultObj != null) {
            isVictory = ((ResultTo) resultObj).victory();
        } else {
            log.trace("'{}' attribute is null", Attr.RESULT);
        }
        log.trace("Victory: {}", isVictory);
        log.trace("Getting session attribute '{}'", Attr.USER);
        Object userObj = req.getSession().getAttribute(Attr.USER);
        UserTo user;
        if (userObj != null) {
            user = (UserTo) userObj;
            log.trace("'{}' is '{}'", Attr.USER, user);
            int victory =isVictory ? user.victory() + 1 : user.victory();
            int defeat =isVictory ? user.defeat() : user.defeat() + 1;
            UserTo newUser = new UserTo(
                    user.id(),
                    user.login(),
                    user.password(),
                    user.role(),
                    victory,
                    defeat,
                    user.image()
            );
            UserService userService = new UserService();
            UserTo updatedUser = SessionFactory.executeInTransaction(() -> {
                userService.update(newUser);
                return userService.get(user.id()).orElseThrow();
            });
            log.trace("Setting session attribute '{}' to '{}'", Attr.USER, updatedUser);
            req.getSession().setAttribute(Attr.USER, updatedUser);
        } else {
            log.trace("'{}' attribute is null", Attr.USER);
        }
        log.trace("Setting session attribute '{}' to 'null'", Attr.QUEST);
        req.getSession().setAttribute(Attr.QUEST, null);
        log.trace("Forwarding to '{}'", LocJSP.RESULT);
        req.getRequestDispatcher(LocJSP.RESULT).forward(req, resp);
    }
}
