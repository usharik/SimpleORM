package ru.usharik.simple.orm;

import ru.usharik.simple.orm.annotation.Column;
import ru.usharik.simple.orm.annotation.Entity;
import ru.usharik.simple.orm.annotation.Id;

@Entity(tableName = "some_table_name")
public class TestEntity {

    @Id
    private Long id;

    @Column(columnName = "field_one")
    private String fieldOne;

    @Column(columnName = "field_two")
    private String fieldTwo;

    @Column(columnName = "field_three")
    private String fieldThree;

    public TestEntity() {
    }

    public TestEntity(Long id, String fieldOne, String fieldTwo, String fieldThree) {
        this.id = id;
        this.fieldOne = fieldOne;
        this.fieldTwo = fieldTwo;
        this.fieldThree = fieldThree;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldOne() {
        return fieldOne;
    }

    public void setFieldOne(String fieldOne) {
        this.fieldOne = fieldOne;
    }

    public String getFieldTwo() {
        return fieldTwo;
    }

    public void setFieldTwo(String fieldTwo) {
        this.fieldTwo = fieldTwo;
    }

    public String getFieldThree() {
        return fieldThree;
    }

    public void setFieldThree(String fieldThree) {
        this.fieldThree = fieldThree;
    }
}
