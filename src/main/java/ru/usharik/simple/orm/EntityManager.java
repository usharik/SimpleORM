package ru.usharik.simple.orm;

import java.sql.SQLException;

public interface EntityManager {

    void persist(Object entity);

    <T> T merge(T entity);

    <T> T findById(long id, Class<T> entityClass) throws SQLException;

    void delete(Object entity);

    void commit() throws SQLException;

    void clear();
}
