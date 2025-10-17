package com.javarush.kazakov.config;

import com.javarush.kazakov.config.constants.Conf;
import jakarta.persistence.Entity;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class EntityFinder {
    private static List<Class<?>> entities;

    private EntityFinder() {
    }

    public static List<Class<?>> getEntities() {
        if (entities == null) {
            EntityFinder entityFinder = new EntityFinder();
            entities = entityFinder.findEntityAnnotatedClasses();
        }
        return entities;
    }

    private List<Class<?>> findEntityAnnotatedClasses() {
        List<Class<?>> classes = new ArrayList<>();
        for (String entityClassName : findEntityClassNames()) {
            try {
                Class<?> entity = Class.forName(entityClassName);
                if (entity.isAnnotationPresent(Entity.class)) {
                    classes.add(entity);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }

    private List<String> findEntityClassNames() {
        try (Stream<Path> paths = Files.walk(getEntityPath())) {
            return paths.filter(Files::isRegularFile)
                    .filter(i -> i.toString().endsWith(Conf.EXT))
                    .map(i -> i.toString().substring(0, i.toString().lastIndexOf(".")))
                    .map(i -> i.substring(i.indexOf(Conf.ENTITY) + Conf.ENTITY.length()))
                    .map(i -> Conf.ENTITY_PACKAGE.concat(i).replace(File.separator, "."))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getEntityPath() {
        try {
            return Path.of(SessionFactory.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .resolve(Conf.ENTITY_PACKAGE.replace(".", File.separator));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
