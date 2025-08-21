package com.javarush.kazakov.repository;

import com.javarush.kazakov.exception.QuestException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

@Slf4j
public class DB {
    private static final String FILE_NAME = "quest";
    private static final String DB_EXTENSION = ".mv.db";
    private static final String INIT_QUESTS_SCHEMA_SQL = "initQuestsSchema.sql";
    private static final String INIT_USERS_SCHEMA_SQL = "initUsersSchema.sql";
    private static final String DRIVER = "org.h2.Driver";
    private static volatile DB instance;
    private final String jdbcUrl;
    private final Path dbPath;

    private DB() {
        try {
            Class.forName(DRIVER);
            log.info("Loaded H2 database driver: {}", DRIVER);
        } catch (ClassNotFoundException e) {
            throw new QuestException("Could not load H2 database driver: %s".formatted(DRIVER), e);
        }
        try {
            dbPath = Path.of(DB.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .resolve(FILE_NAME + DB_EXTENSION);
            jdbcUrl = "jdbc:h2:" + dbPath.getParent().resolve(FILE_NAME);
        } catch (URISyntaxException e) {
            throw new QuestException("Error creating jdbc url", e);
        }
    }

    private void generateDefaultDB(String... scriptName) {
        log.info("Generating default database...");
        Path scriptPath = null;
        for (String script : scriptName) {
            try {
                scriptPath = Path.of(DB.class.getProtectionDomain().getCodeSource().getLocation().toURI()).resolve(script);
            } catch (URISyntaxException e) {
                throw new QuestException("Unable to find script " + script, e);
            }
            try (Connection connection = getConnection()) {
                String sql = Files.readString(scriptPath);
                log.trace("Executing SQL script: {}", scriptPath);
                connection.createStatement().execute(sql);
                log.trace("Executing SQL script done.");
            } catch (IOException | SQLException e) {
                throw new QuestException("Unable to read default SQL script '%s'".formatted(scriptPath), e);
            }
        }
    }

    public void restoreDefaultDBFile() {
        log.trace("Restoring default database state");
        try {
            if (Files.deleteIfExists(dbPath)){
                log.info("Deleted H2 database file: {}", dbPath);
                instance.generateDefaultDB(INIT_USERS_SCHEMA_SQL, INIT_QUESTS_SCHEMA_SQL);
            }
        } catch (IOException e) {
            throw new QuestException("Unable to delete db file '%s%s'".formatted(FILE_NAME, DB_EXTENSION), e);
        }
    }

    public static DB getInstance() {
        if (instance == null) {
            synchronized (DB.class) {
                if (instance == null) {
                    log.trace("Creating new instance");
                    instance = new DB();
                    if (!instance.dbPath.toFile().exists()) {
                        log.info("{} not found. Initializing database", instance.dbPath);
                        instance.generateDefaultDB(INIT_USERS_SCHEMA_SQL, INIT_QUESTS_SCHEMA_SQL);
                    }
                }
            }
        }
        return instance;
    }

    public static Connection getConnection() {
        if (instance == null) {
            getInstance();
        }
        try {
            log.trace("Connecting to database.");
            return DriverManager.getConnection(instance.jdbcUrl);
        } catch (SQLException e) {
            throw new QuestException("Error creating connection", e);
        }
    }
}
