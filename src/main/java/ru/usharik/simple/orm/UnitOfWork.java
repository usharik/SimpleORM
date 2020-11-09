package ru.usharik.simple.orm;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UnitOfWork {

    private final List<String> unitOfWorkList = new ArrayList<>();

    private final Connection conn;

    public UnitOfWork(Connection conn) {
        this.conn = conn;
    }

    public void add(String unitOfWork) {
        unitOfWorkList.add(unitOfWork);
    }

    public void commit() throws SQLException {
        conn.setAutoCommit(false);
        try (Statement stmt = conn.createStatement()) {
            for (String query : unitOfWorkList) {
                stmt.addBatch(query);
            }
            stmt.executeBatch();
            conn.commit();
            clear();
        } catch (Exception ex) {
            conn.rollback();
            throw new IllegalStateException("Can't fix transaction", ex);
        }
    }

    public void clear() {
        unitOfWorkList.clear();
    }
}
