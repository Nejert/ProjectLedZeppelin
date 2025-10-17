package com.javarush.kazakov.config;

public class BaseTest {
    public static TestContainer container;
    public static org.hibernate.SessionFactory sessionFactory;

    public static void init() {
        container = new TestContainer();
        container.start();
        sessionFactory = container.getSessionFactory();
    }

    public static void destroy() {
        container.close();
    }
}
