package ru.usharik.simple.orm;

import ru.usharik.simple.orm.entity.EntityDescriptor;
import ru.usharik.simple.orm.entity.EntityRegister;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

class EntityManagerImpl implements EntityManager {

    private final EntityRegister entityRegister;
    private final Connection conn;

    private final SqlDialect dialect;

    private final Map<Long, Object> identityMap = new HashMap<>();

    private final UnitOfWork unitOfWork;

    public EntityManagerImpl(UnitOfWork unitOfWork, EntityRegister entityRegister,
                             Connection conn, SqlDialect dialect) {
        this.unitOfWork = unitOfWork;
        this.entityRegister = entityRegister;
        this.conn = conn;
        this.dialect = dialect;
    }

    @Override
    public void persist(Object entity) {
        EntityDescriptor ed = entityRegister.getEntityDescriptor(entity.getClass());
        Long keyValue = ed.getKeyValue(entity);
        if (!identityMap.containsKey(keyValue)) {
            identityMap.put(keyValue, entity);
            unitOfWork.add(dialect.buildInsertQuery(
                    ed.getTableName(),
                    ed.getColumnInfoList(),
                    ed.buildColumnValuesList(entity)
            ));
        } else {
            throw new IllegalStateException("Entity with id " + keyValue + " already managed.");
        }
    }

    @Override
    public <T> T merge(T entity) {
        EntityDescriptor ed = entityRegister.getEntityDescriptor(entity.getClass());
        Long keyValue = ed.getKeyValue(entity);
        if (!identityMap.containsKey(keyValue)) {
            persist(entity);
        } else {
            identityMap.put(keyValue, entity);
            unitOfWork.add(dialect.buildUpdateQuery(
                    ed.getTableName(),
                    ed.getKeyValue(entity),
                    ed.getColumnInfoList(),
                    ed.buildColumnValuesList(entity)
            ));
        }
        return entity;
    }

    @Override
    public <T> T findById(long id, Class<T> entityClass) throws SQLException {
        EntityDescriptor ed = entityRegister.getEntityDescriptor(entityClass);
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(dialect.buildSelectByIdQuery(
                ed.getTableName(),
                id
        ));
        return (T) ed.createEntityByResultSet(resultSet);
    }

    @Override
    public void delete(Object entity) {
        EntityDescriptor ed = entityRegister.getEntityDescriptor(entity.getClass());
        identityMap.remove(ed.getKeyValue(entity));
        unitOfWork.add(dialect.buildDeleteQuery(
                ed.getTableName(),
                ed.getKeyValue(entity)
        ));
    }

    @Override
    public void commit() throws SQLException {
        unitOfWork.commit();
    }

    @Override
    public void clear() {
        identityMap.clear();
        unitOfWork.clear();
    }
}
