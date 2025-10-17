package com.javarush.kazakov.config;

import com.javarush.kazakov.util.QuestUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.testcontainers.containers.PostgreSQLContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

@Getter
@Slf4j
public class TestContainer {
    private org.hibernate.SessionFactory sessionFactory;
    private final PostgreSQLContainer<?> container;
    private final Properties properties;
    private final Configuration config;
    public static final String DOCKER_IMAGE_NAME = "postgres:latest";

    public TestContainer() {
        config = HibernateConfiguration.getConfiguration();
        properties = config.getProperties();

        container = new PostgreSQLContainer<>(DOCKER_IMAGE_NAME)
                .withDatabaseName("")
                .withUsername(properties.getProperty(Environment.JAKARTA_JDBC_USER))
                .withPassword(properties.getProperty(Environment.JAKARTA_JDBC_PASSWORD));
    }

    public void start() {
        container.start();

        properties.setProperty(Environment.JAKARTA_JDBC_URL, container.getJdbcUrl());
        properties.setProperty(Environment.JAKARTA_JDBC_USER, container.getUsername());
        properties.setProperty(Environment.JAKARTA_JDBC_PASSWORD, container.getPassword());

        try {
            new LiquibaseInit(properties).initDefaultDB();
        } catch (Exception e) {
            log.error("Unable to initialize Database", e);
            throw new RuntimeException(e);
        }
        properties.putAll(QuestUtil.enableP6spy(properties));
    }

    public void close() {
        if (container != null && container.isRunning())
            container.close();
    }

    public org.hibernate.SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            config.setProperties(properties);
            sessionFactory = config.buildSessionFactory();
            setOriginalSessionFactory(sessionFactory);
        }
        return sessionFactory;
    }

    private void setOriginalSessionFactory(org.hibernate.SessionFactory fake) {
        Class<SessionFactory> sessionFactoryClass = SessionFactory.class;
        try {
            Constructor<SessionFactory> sessionFactoryConstructor = sessionFactoryClass.getDeclaredConstructor();
            if (sessionFactoryConstructor.trySetAccessible()) {
                SessionFactory origin = sessionFactoryConstructor.newInstance();
                Field field = sessionFactoryClass.getDeclaredField("sessionFactory");
                if (field.trySetAccessible()) {
                    field.set(origin, fake);
                }
            }
        } catch (NoSuchFieldException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            log.error("Failed to replace session factory", e);
            throw new RuntimeException(e);
        }
    }
}
