package ru.usharik.simple.orm.entity;

public class ColumnInfo {

    private final String classFieldName;

    private final String dbColumnName;

    private final Class<?> type;

    private final boolean isPrimaryKey;

    public ColumnInfo(String classFieldName, String dbColumnName, Class<?> type, boolean isPrimaryKey) {
        this.classFieldName = classFieldName;
        this.dbColumnName = dbColumnName;
        this.type = type;
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getClassFieldName() {
        return classFieldName;
    }

    public String getDbColumnName() {
        return dbColumnName;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }
}
