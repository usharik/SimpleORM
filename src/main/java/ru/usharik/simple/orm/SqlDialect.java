package ru.usharik.simple.orm;

import ru.usharik.simple.orm.entity.ColumnInfo;

import java.util.List;

public interface SqlDialect {
    String buildCreateTableQuery(String tableName, List<ColumnInfo> columns);

    String buildSelectAllQuery(String tableName);

    String buildSelectByIdQuery(String tableName, Long id);

    String buildInsertQuery(String tableName, List<ColumnInfo> columns, List<Object> values);

    String buildUpdateQuery(String tableName, Long id, List<ColumnInfo> columns, List<Object> values);

    String buildDeleteQuery(String tableName, Long id);
}
