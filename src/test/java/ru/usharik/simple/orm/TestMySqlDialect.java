package ru.usharik.simple.orm;

import org.junit.Test;
import ru.usharik.simple.orm.entity.EntityDescriptor;

import java.util.Arrays;

public class TestMySqlDialect {

    private final SqlDialect sqlDialect = new MySqlDialect();

    private final EntityDescriptor ed = new EntityDescriptor(TestEntity.class);

    @Test
    public void testCreateTable() {
        System.out.println(sqlDialect.buildCreateTableQuery(ed.getTableName(), ed.getColumnInfoList()));
    }

    @Test
    public void testInsertQuery() {
        System.out.println(sqlDialect.buildInsertQuery(ed.getTableName(), ed.getColumnInfoList(), Arrays.asList(1, "2", "3", "4")));
    }

    @Test
    public void testUpdateQuery() {
        System.out.println(sqlDialect.buildUpdateQuery(ed.getTableName(), 1L, ed.getColumnInfoList(), Arrays.asList("1", "2", "3")));
    }
}
