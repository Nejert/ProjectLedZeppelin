package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.SessionFactory;
import com.javarush.kazakov.config.constants.Attr;
import com.javarush.kazakov.config.constants.Loc;
import com.javarush.kazakov.config.constants.LocJSP;
import com.javarush.kazakov.dto.user.UserTo;
import com.javarush.kazakov.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@WebServlet(Loc.STATS)
public class StatisticsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        if (req.getSession().getAttribute(Attr.USER) == null) {
            log.trace("User is null, redirecting to '{}' page", Loc.SIGN_IN);
            resp.sendRedirect(Loc.SIGN_IN);
        } else {
            log.trace("Forwarding to '{}'", LocJSP.STATISTICS);
            UserService userService = new UserService();
            Map<UserTo, Integer> allUsersNumberQuests =
                    SessionFactory.executeInTransaction(() -> userService.getAll().stream()
                    .collect(Collectors.toMap(i -> i, userService::getQuestQuantity)));
            req.getSession().setAttribute(Attr.USERS, allUsersNumberQuests);
            req.getRequestDispatcher(LocJSP.STATISTICS).forward(req, resp);
        }
    }
}
