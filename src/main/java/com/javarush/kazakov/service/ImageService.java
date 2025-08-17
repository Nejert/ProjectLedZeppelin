package com.javarush.kazakov.service;

import com.javarush.kazakov.exception.QuestException;
import jakarta.servlet.http.Part;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@NoArgsConstructor
public class ImageService {
    private static final String IMAGES_FOLDER = "images";
    private static final String NO_IMAGE_PNG = "no-image.png";

    public String loadImage(String login, Part imageParts) {
        log.trace("Loading image");
        if (imageParts.getSize() == 0 || imageParts.getSubmittedFileName().isEmpty()) {
            log.warn("There's no image in request. Returning '{}'", NO_IMAGE_PNG);
            return NO_IMAGE_PNG;
        }
        URL classes = ImageService.class.getResource("/");
        Path imageFilePath;
        try {
            imageFilePath = Path.of(classes.toURI()).getParent().getParent().resolve(IMAGES_FOLDER);
            log.trace("Image loads to {}", imageFilePath);
        } catch (URISyntaxException e) {
            throw new QuestException("Could not find image folder", e);
        }
        String submittedFileName = imageParts.getSubmittedFileName();
        String extension = submittedFileName.substring(submittedFileName.indexOf('.'));
        String newFileName = login + extension;
        imageFilePath = imageFilePath.resolve(newFileName);
        log.trace("New image file path '{}'", imageFilePath);
        try (InputStream imagePartsInputStream = imageParts.getInputStream()) {
            log.trace("Copying file from stream to file.");
            Files.copy(imagePartsInputStream, imageFilePath, StandardCopyOption.REPLACE_EXISTING);
            log.trace("Copying done");
        } catch (IOException e) {
            throw new QuestException("Could not load image file", e);
        }
        log.trace("Returning '{}'", newFileName);
        return newFileName;
    }
}
