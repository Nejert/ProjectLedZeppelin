package com.javarush.kazakov.repository;


import com.javarush.kazakov.config.SessionFactory;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<T> implements Repository<T> {
    protected final Class<T> aClass;
    protected final Session session;

    public AbstractRepository(Class<T> aClass) {
        this.aClass = aClass;
        session = SessionFactory.getSessionFactory().getCurrentSession();
    }

    @Override
    public List<T> getAll() {
        return session.createQuery("from " + aClass.getSimpleName(), aClass).list();
    }

    @Override
    public Optional<T> get(long id) {
        return Optional.ofNullable(session.find(aClass, id));
    }

    @Override
    public int getCount() {
        return (int)(long) session.createQuery("select count(*) from "+aClass.getSimpleName(), Long.class).uniqueResult();
    }

    @Override
    public void save(T t) {
        session.persist(t);
    }

    @Override
    public void update(T t) {
        session.merge(t);
    }

    @Override
    public void delete(T t) {
        session.remove(t);
    }

    @Override
    public void delete(long id) {
        session.remove(get(id));
    }
}
