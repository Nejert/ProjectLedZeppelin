package com.javarush.kazakov.repository;

import com.javarush.kazakov.entity.user.User;

import java.util.Optional;

public class UserRepository extends AbstractRepository<User> {

    public UserRepository() {
        super(User.class);
    }

    public int countAuthorship(int id) {
        return (int) (long) session.createQuery("select count(q) from Quest q " +
                        "where q.author.id = :id", Long.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public Optional<User> get(String login, String password) {
        return Optional.ofNullable(
                session.createQuery("select u from User u " +
                                "where u.login = :login and u.password = :password", User.class)
                        .setParameter("login", login)
                        .setParameter("password", password)
                        .uniqueResult()
        );
    }
}
