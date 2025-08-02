package com.javarush.kazakov.repository;

import com.javarush.kazakov.exception.QuestException;

import java.io.IOException;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;


public class DB {
    private static final String FILE_NAME = "quest";
    private static final String DB_EXTENSION = ".mv.db";
    private static final String INIT_QUESTS_SCHEMA_SQL = "initQuestsSchema.sql";
    private static final String INIT_USERS_SCHEMA_SQL = "initUsersSchema.sql";
    private static volatile DB instance;
    private final String jdbcUrl;
    private final Path dbPath;

    private DB() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
        Path scriptPath = null;
        for (String script : scriptName) {
            try {
                scriptPath = Path.of(DB.class.getProtectionDomain().getCodeSource().getLocation().toURI()).resolve(script);
            } catch (URISyntaxException e) {
                throw new QuestException("Unable to find script " + script, e);
            }
            try (Connection connection = getConnection()) {
                String sql = Files.readString(scriptPath);
                connection.createStatement().execute(sql);
            } catch (IOException | SQLException e) {
                throw new QuestException("Unable to read default SQL script", e);
            }
        }
    }

    public static DB getInstance() {
        if (instance == null) {
            synchronized (DB.class) {
                if (instance == null) {
                    instance = new DB();
                    if (!instance.dbPath.toFile().exists()) {
                        instance.generateDefaultDB(INIT_QUESTS_SCHEMA_SQL, INIT_USERS_SCHEMA_SQL);
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
            return DriverManager.getConnection(instance.jdbcUrl);
        } catch (SQLException e) {
            throw new QuestException("Error creating connection", e);
        }
    }


}
