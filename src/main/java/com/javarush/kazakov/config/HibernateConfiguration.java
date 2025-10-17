package com.javarush.kazakov.config;

import com.javarush.kazakov.config.constants.Conf;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class HibernateConfiguration {
    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        try {
            PROPERTIES.load(HibernateConfiguration.class.getResourceAsStream(Conf.HIBERNATE_PROPERTIES));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        checkEnvironment();
    }

    private static void checkEnvironment() {
        Map<String, String> env = System.getenv();
        for (Map.Entry<Object, Object> entry : PROPERTIES.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            if (value.startsWith("${") && value.endsWith("}")) {
                String envVar = value.substring(2, value.indexOf(':'));
                String defaultValue = value.substring(value.indexOf(':') + 1, value.length() - 2);
                String envVal = env.get(envVar);
                PROPERTIES.setProperty(key, Objects.requireNonNullElse(envVal, defaultValue));
            }
        }
    }

    public static Configuration getConfiguration() {
        Configuration conf = new Configuration();
        conf.setProperties((Properties) PROPERTIES.clone());
        Conf.ENTITIES.forEach(conf::addAnnotatedClass);
        return conf;
    }
}
