package ru.usharik.simple.orm;

import ru.usharik.simple.orm.entity.ColumnInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MySqlDialect implements SqlDialect {

    private static final Map<Class<?>, String> typesMap = buildTypesMap();

    private static final Map<Class<?>, Function<Object, String>> typeConvertersMap = buildTypeConvertersMap();

    public String buildCreateTableQuery(String tableName, List<ColumnInfo> columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("create table if not exists ");
        sb.append(tableName);
        sb.append("(");
        for (ColumnInfo columnInfo : columns) {
            sb.append(columnInfo.getDbColumnName());
            sb.append(" ");
            sb.append(typesMap.get(columnInfo.getType()));
            if (columnInfo.isPrimaryKey()) {
                sb.append(" primary key ");
            }
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");");
        return sb.toString();
    }

    @Override
    public String buildSelectAllQuery(String tableName) {
        return  "select * from " +
                tableName +
                ";";
    }

    @Override
    public String buildSelectByIdQuery(String tableName, Long id) {
        return  "select * from " +
                tableName +
                " where id = " +
                id +
                ";";
    }

    public String buildInsertQuery(String tableName, List<ColumnInfo> columns, List<Object> values) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(tableName);
        sb.append(" (");
        for (ColumnInfo columnInfo : columns) {
            sb.append(columnInfo.getDbColumnName());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") ");
        sb.append(" values (");
        for (Object val : values) {
            sb.append(valueConverter(val));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(");");
        return sb.toString();
    }

    public String buildUpdateQuery(String tableName, Long id, List<ColumnInfo> columns, List<Object> values) {
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(tableName);
        sb.append(" set ");
        for (int i = 0; i < values.size(); i++) {
            sb.append(columns.get(i).getDbColumnName());
            sb.append(" = ");
            sb.append(valueConverter(values.get(i)));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" where id = ");
        sb.append(id);
        sb.append(";");
        return sb.toString();
    }

    public String buildDeleteQuery(String tableName, Long id) {
        return  "delete from " +
                tableName +
                " where id = " +
                id +
                ";";
    }

    private String valueConverter(Object value) {
        return typeConvertersMap.getOrDefault(value.getClass(),Object::toString).apply(value);
    }

    private static Map<Class<?>, String> buildTypesMap() {
        Map<Class<?>, String> result = new HashMap<>();
        result.put(Integer.class, "INT");
        result.put(Long.class, "BIGINT");
        result.put(String.class, "NVARCHAR(1024)");
        return result;
    }

    private static Map<Class<?>, Function<Object, String>> buildTypeConvertersMap() {
        Map<Class<?>, Function<Object, String>> result = new HashMap<>();
        result.put(String.class, obj -> String.format("'%s'", obj));
        return result;
    }
}
