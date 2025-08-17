package com.javarush.kazakov.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuestException extends RuntimeException {
    public QuestException(String message, Throwable cause) {
        super(message, cause);
        log.error("{} -> {}", message, cause.getMessage());
    }

    public QuestException(String message) {
        super(message);
        log.error(message);
    }
}
