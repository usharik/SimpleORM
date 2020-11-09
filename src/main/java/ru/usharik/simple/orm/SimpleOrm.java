package ru.usharik.simple.orm;

import ru.usharik.simple.orm.entity.EntityRegister;

import java.sql.Connection;

public class SimpleOrm {

    private static final EntityRegister entityRegister = new EntityRegister();

    public static EntityManagerFactory createEntityManagerFactory(Connection conn, SqlDialect dialect) {
        return new EntityManagerFactoryImpl(conn, dialect, entityRegister);
    }

    public void registerEntity(Class<?> entityClass) {
        entityRegister.register(entityClass);
    }
}
