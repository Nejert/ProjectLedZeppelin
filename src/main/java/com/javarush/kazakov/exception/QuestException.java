package com.javarush.kazakov.exception;

public class QuestException extends RuntimeException {
    public QuestException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestException(String message) {
        super(message);
    }
}
