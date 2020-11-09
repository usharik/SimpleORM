package ru.usharik.simple.orm;

import ru.usharik.simple.orm.entity.EntityRegister;

import java.sql.Connection;

class EntityManagerFactoryImpl implements EntityManagerFactory {

    private final Connection conn;

    private final SqlDialect dialect;

    private final EntityRegister entityRegister;

    public EntityManagerFactoryImpl(Connection conn, SqlDialect dialect, EntityRegister entityRegister) {
        this.conn = conn;
        this.dialect = dialect;
        this.entityRegister = entityRegister;
    }

    @Override
    public EntityManager createEntityManager() {
        return new EntityManagerImpl(new UnitOfWork(conn), entityRegister, conn, dialect);
    }
}
