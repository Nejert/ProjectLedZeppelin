package com.javarush.kazakov.config;

import com.javarush.kazakov.exception.QuestException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
public class JSConstantsGenerator {
    private static final String EXPORT = "export";
    private static final String CONST = "const";

    public void generate(Class<?> clazz, String fileName) {
        log.trace("Generating JS constants from '{}.class' to '{}' file", clazz.getSimpleName(), fileName);
        try (InputStream inputStream = new ByteArrayInputStream(gatherData(clazz))) {
            Files.copy(inputStream, getPath(fileName), StandardCopyOption.REPLACE_EXISTING);
            log.trace("Writing done");
        } catch (IOException e) {
            throw new QuestException("Unable to write file %s".formatted(fileName), e);
        }
    }

    private byte[] gatherData(Class<?> clazz) {
        log.trace("Gathering data from '{}.class'", clazz.getSimpleName());
        StringBuilder data = new StringBuilder();
        for (Field declaredField : clazz.getDeclaredFields()) {
            try {
                data.append(EXPORT).append(" ").append(CONST).append(" ")
                        .append(declaredField.getName()).append(" = \"")
                        .append(declaredField.get(declaredField)).append("\";\n");
            } catch (IllegalAccessException e) {
                throw new QuestException("Access to field '%s' is forbidden".formatted(declaredField.getName()), e);
            }
        }
        log.trace("Returning byte array of gathered data");
        log.trace(data.toString());
        return data.toString().getBytes(StandardCharsets.UTF_8);
    }

    private Path getPath(String fileName) {
        try {
            Path jsFolderPath = Path.of(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().resolve("../../js/").resolve(fileName));
            log.trace("Found js folder: '{}'", jsFolderPath);
            return jsFolderPath;
        } catch (URISyntaxException e) {
            throw new QuestException("Unable to find js folder", e);
        }
    }
}
