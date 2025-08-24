package com.javarush.kazakov.controller;

import com.javarush.kazakov.config.constants.Loc;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebServlet(Loc.SIGN_OUT)
public class SignOutController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.trace("URI:{} -> method:{}", req.getRequestURI(), req.getMethod());
        log.trace("Invalidating session");
        req.getSession().invalidate();
        log.trace("Redirecting to '/' page");
        resp.sendRedirect("/");
    }
}
