package com.javarush.kazakov.config.constants;

import com.javarush.kazakov.config.EntityFinder;

import java.util.List;

public class Conf {
    public static final String ENTITY = "entity";
    public static final String EXT = ".class";
    public static final String ENTITY_PACKAGE = "com.javarush.kazakov." + ENTITY;
    public static final String HIBERNATE_PROPERTIES = "/hibernate.properties";
    public static final List<Class<?>> ENTITIES = EntityFinder.getEntities();
    public static final String CHANGELOG_FILE = "/liquibase/changelog.xml";
    public static final String P6SPY_DRIVER = "com.p6spy.engine.spy.P6SpyDriver";
    private Conf() {
    }
}
