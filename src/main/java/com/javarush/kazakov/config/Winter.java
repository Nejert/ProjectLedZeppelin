package com.javarush.kazakov.config;

import com.javarush.kazakov.exception.QuestException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Winter {

    private Winter(){}

    private static final Map<Class<?>, Object> components = new HashMap<>();

    public static <T> T find(Class<T> aClass) {
        log.trace("Finding component {}", aClass.getSimpleName());
        Object component = components.get(aClass);
        if (component == null) {
            log.trace("There's no component {}", aClass.getSimpleName());
            Constructor<?> constructor = aClass.getConstructors()[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = Winter.find(parameterTypes[i]);
            }
            Object newInstance = null;
            log.trace("Trying to instantiate component {}", aClass.getSimpleName());
            try {
                newInstance = constructor.newInstance(parameters);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new QuestException("Could not create new instance of " + aClass, e);
            }
            log.trace("Putting component {} -> {} to components map", aClass.getSimpleName(), newInstance);
            components.put(aClass, newInstance);
        }
        try {
            T result = (T) components.get(aClass);
            log.trace("Returning {}", result);
            return result;
        } catch (ClassCastException | NullPointerException e) {
            throw new QuestException("Could not cast " + aClass + " to " + Objects.requireNonNull(component).getClass(), e);
        }
    }
}
