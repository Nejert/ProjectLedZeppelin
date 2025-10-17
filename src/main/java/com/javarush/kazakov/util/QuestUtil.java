package com.javarush.kazakov.util;

import com.javarush.kazakov.config.constants.Conf;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class QuestUtil {
    private QuestUtil() {
    }

    public static Properties enableP6spy(Properties properties) {
        Properties p = new Properties(properties);
        p.setProperty(Environment.JAKARTA_JDBC_DRIVER, Conf.P6SPY_DRIVER);
        p.setProperty(Environment.JAKARTA_JDBC_URL, p.getProperty(Environment.JAKARTA_JDBC_URL).replace("jdbc:", "jdbc:p6spy:"));
        return p;
    }
}
