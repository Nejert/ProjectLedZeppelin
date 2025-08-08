package com.javarush.kazakov.service;

import com.javarush.kazakov.exception.QuestException;
import jakarta.servlet.http.Part;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@NoArgsConstructor
public class ImageService {
    private static final String IMAGES_FOLDER = "images";
    private static final String NO_IMAGE_PNG = "no-image.png";

    public String loadImage(String login, Part imageParts) {
        if (imageParts.getSize() == 0 || imageParts.getSubmittedFileName().isEmpty()) {
            return NO_IMAGE_PNG;
        }
        URL classes = ImageService.class.getResource("/");
        Path imageFilePath;
        try {
            imageFilePath = Path.of(classes.toURI()).getParent().getParent().resolve(IMAGES_FOLDER);
        } catch (URISyntaxException e) {
            throw new QuestException("Could not find image folder -> ", e);
        }
        String submittedFileName = imageParts.getSubmittedFileName();
        String extension = submittedFileName.substring(submittedFileName.indexOf('.'));
        String newFileName = login + extension;
        imageFilePath = imageFilePath.resolve(newFileName);
        try (InputStream imagePartsInputStream = imageParts.getInputStream()) {
            Files.copy(imagePartsInputStream, imageFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new QuestException("Could not load image file -> ", e);
        }
        return newFileName;
    }
}
