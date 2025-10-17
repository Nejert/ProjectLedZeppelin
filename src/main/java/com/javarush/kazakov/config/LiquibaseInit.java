package com.javarush.kazakov.config;

import com.javarush.kazakov.config.constants.Conf;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;

import java.util.Properties;

@Slf4j
public class LiquibaseInit {
    private final Properties properties;

    public LiquibaseInit(Properties properties) {
        this.properties = properties;
    }

    public void initDefaultDB() throws Exception {
        log.info("Running Liquibase...");
        Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
            CommandScope update = new CommandScope("update");

            update.addArgumentValue("changelogFile", Conf.CHANGELOG_FILE);
            update.addArgumentValue("url", properties.getProperty(Environment.JAKARTA_JDBC_URL));
            update.addArgumentValue("username", properties.getProperty(Environment.JAKARTA_JDBC_USER));
            update.addArgumentValue("password", properties.getProperty(Environment.JAKARTA_JDBC_PASSWORD));

            update.execute();
        });
        log.info("Running Liquibase...DONE");
    }
}
