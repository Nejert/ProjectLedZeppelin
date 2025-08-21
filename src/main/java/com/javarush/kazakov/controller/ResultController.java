package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.Winter;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.entity.Result;
import com.javarush.kazakov.entity.User;
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
    UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.trace("Initializing Servlet");
        super.init(config);
        userService = Winter.find(UserService.class);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        log.trace("Getting session attribute: '{}'", Attr.RESULT);
        Object resultObj = req.getSession().getAttribute(Attr.RESULT);
        boolean isVictory = false;
        if (resultObj != null) {
            isVictory = ((Result) resultObj).isVictory();
        } else {
            log.trace("'{}' attribute is null", Attr.RESULT);
        }
        log.trace("Victory: {}", isVictory);
        log.trace("Getting session attribute '{}'", Attr.USER);
        Object userObj = req.getSession().getAttribute(Attr.USER);
        User user = null;
        if (userObj != null) {
            user = (User) userObj;
            log.trace("'{}' is '{}'", Attr.USER, user);
            User userUpdated = User.builder()
                    .login(user.getLogin())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .victory(isVictory ? user.getVictory() + 1 : user.getVictory())
                    .defeat(isVictory ? user.getDefeat() : user.getDefeat() + 1)
                    .image(user.getImage())
                    .questQuantity(user.getQuestQuantity())
                    .build();
            userService.update(userUpdated);
            User repoUpdatedUser = userService.get(userUpdated.getLogin());
            log.trace("Setting session attribute '{}' to '{}'", Attr.USER, repoUpdatedUser);
            req.getSession().setAttribute(Attr.USER, repoUpdatedUser);
        } else {
            log.trace("'{}' attribute is null", Attr.USER);
        }
        log.trace("Setting session attribute '{}' to 'null'", Attr.QUEST);
        req.getSession().setAttribute(Attr.QUEST, null);
        log.trace("Forwarding to '{}'", LocJSP.RESULT);
        req.getRequestDispatcher(LocJSP.RESULT).forward(req, resp);
    }
}
