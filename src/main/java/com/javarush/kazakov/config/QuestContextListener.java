package com.javarush.kazakov.config;

import com.javarush.kazakov.config.constants.Param;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class QuestContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Winter.find(JSConstantsGenerator.class).generate(Param.class, "param.js");
    }
}
