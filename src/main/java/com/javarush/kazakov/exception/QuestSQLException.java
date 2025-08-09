package com.javarush.kazakov.exception;

public class QuestSQLException extends QuestException {

    public QuestSQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestSQLException(String message) {
        super(message);
    }
}
