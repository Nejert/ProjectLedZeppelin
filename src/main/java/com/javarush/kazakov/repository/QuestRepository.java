package com.javarush.kazakov.repository;

import com.javarush.kazakov.entity.quest.QuestItem;

import java.util.Optional;

public class QuestRepository<T extends QuestItem> extends AbstractRepository<T> {

    public QuestRepository(Class<T> aClass) {
        super(aClass);
    }

    @Override
    public Optional<T> get(String name, String attr) {
        return Optional.ofNullable(
                session.createQuery("select i from "+aClass.getSimpleName()+" i where lower(i.title) = :name", aClass)
                        .setParameter("name", name.toLowerCase()).uniqueResult()
        );
    }

    public Optional<T> get(String name) {
        return get(name, null);
    }
}
