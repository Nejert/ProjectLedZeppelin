package com.javarush.kazakov.config;

import com.javarush.kazakov.util.QuestUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Properties;
import java.util.concurrent.Callable;

@Slf4j
public class SessionFactory {
    private static org.hibernate.SessionFactory sessionFactory;

    private SessionFactory() {
    }

    public static org.hibernate.SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            System.getenv().forEach((k, v) -> log.info("{}={}", k, v));
            Configuration configuration = HibernateConfiguration.getConfiguration();
            Properties properties = configuration.getProperties();
            try {
                new LiquibaseInit(properties).initDefaultDB();
            } catch (Exception e) {
                log.error("Unable to initialize Database", e);
                throw new RuntimeException(e);
            }
            properties.putAll(QuestUtil.enableP6spy(properties));
            configuration.setProperties(properties);
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void executeInTransaction(Runnable runnable) {
        Transaction transaction = SessionFactory.getSessionFactory().getCurrentSession().beginTransaction();
        runnable.run();
        transaction.commit();
    }

    public static <V> V executeInTransaction(Callable<V> callable) {
        V result;
        Transaction transaction = SessionFactory.getSessionFactory().getCurrentSession().beginTransaction();
        try {
            result = callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        transaction.commit();
        return result;
    }
}
