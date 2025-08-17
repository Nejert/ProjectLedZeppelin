package com.javarush.kazakov.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class ErrorCleanerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(req, res);
        log.trace("Getting session");
        HttpSession session = req.getSession(false);
        if (req.getMethod().equals("GET") && session != null) {
            log.trace("Removing session attribute '{}'", "errorMessage");
            session.removeAttribute("errorMessage");
        } else {
            log.trace("Session is null, continue");
        }
    }
}
