package ru.usharik.simple.orm.entity;

import ru.usharik.simple.orm.annotation.Column;
import ru.usharik.simple.orm.annotation.Entity;
import ru.usharik.simple.orm.annotation.Id;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDescriptor {

    private final List<ColumnInfo> columnInfoList;

    private final Class<?> entityClass;

    private final String tableName;

    private final Field idField;

    public EntityDescriptor(Class<?> entityClass) {
        this.entityClass = entityClass;
        this.tableName = getTableNameForEntity();
        this.columnInfoList = buildColumnListForEntity();

        this.idField = Arrays.stream(entityClass.getDeclaredFields())
                .filter(fld -> fld.getAnnotation(Id.class) != null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not @Id in the entity"));
    }

    private List<ColumnInfo> buildColumnListForEntity() {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(fld -> fld.getAnnotation(Column.class) != null || fld.getAnnotation(Id.class) != null)
                .map(fld -> new ColumnInfo(fld.getName(),
                        fld.getAnnotation(Id.class) == null ? fld.getAnnotation(Column.class).columnName() : "id",
                        fld.getType(),
                        fld.getAnnotation(Id.class) != null
                ))
                .collect(Collectors.toList());
    }

    private String getTableNameForEntity() {
        Entity entity = entityClass.getAnnotation(Entity.class);
        return entity.tableName();
    }

    public Long getKeyValue(Object entity) {
        return invokeGetter(this.idField.getName(), entity.getClass(), entity);
    }

    private static <R> void invokeSetter(String fieldName, R value, Class<?> paramType, Class<?> entityClass, Object entity) {
        try {
            Method setter = entityClass.getMethod(buildSetterName(fieldName), paramType);
            setter.invoke(entity, value);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static <R> R invokeGetter(String fieldName, Class<?> entityClass, Object entity) {
        try {
            Method setter =entityClass.getMethod(buildGetterName(fieldName));
            return (R) setter.invoke(entity);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private static String buildSetterName(String fieldName) {
        return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private static String buildGetterName(String fieldName) {
        return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    public List<Object> buildColumnValuesList(Object entity) {
        return this.columnInfoList
                .stream()
                .map(info -> invokeGetter(info.getClassFieldName(), entity.getClass(), entity))
                .collect(Collectors.toList());
    }

    public Object createEntityByResultSet(ResultSet rs) throws SQLException {
        Object entity = newInstance();
        for (ColumnInfo columnInfo : columnInfoList) {
            invokeSetter(columnInfo.getClassFieldName(),
                    rs.getObject(columnInfo.getDbColumnName(), columnInfo.getType()),
                    columnInfo.getType(),
                    entityClass,
                    entity);
        }
        return entity;
    }

    private Object newInstance() {
        try {
            return this.entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
    }
}
