package com.javarush.kazakov.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    List<T> getAll();

    Optional<T> get(long id);

    Optional<T> get(String name, String attr);

    int getCount();

    void save(T t);

    void update(T t);

    void delete(T t);

    void delete(long id);
}
