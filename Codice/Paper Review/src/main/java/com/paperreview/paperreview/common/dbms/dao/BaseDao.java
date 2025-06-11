package com.paperreview.paperreview.common.dbms.dao;

import com.paperreview.paperreview.entities.BaseEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao <T extends BaseEntity> {
    protected Connection connection;
    protected final String tableName;
    protected final String idColumn;

    public BaseDao(Connection connection, String tableName, String idColumn) {
        this.connection = connection;
        this.tableName = tableName;
        this.idColumn = idColumn;
    }

    public List<T> getAll() {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName;

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public T getById(int id) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void save(T entity) throws SQLException{
        try (PreparedStatement stmt = connection.prepareStatement(getInsertQuery(), Statement.RETURN_GENERATED_KEYS)) {
            prepareInsert(stmt, entity);
            stmt.executeUpdate();

            // Recupero ID generato
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                setGeneratedId(entity, generatedId);
            }

        }
    }

    public void update(T entity) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(getUpdateQuery())) {
            prepareUpdate(stmt, entity);
            stmt.executeUpdate();
        }
    }

    public void delete(T entity) throws SQLException {
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entity.getId());
            stmt.executeUpdate();
        }
    }

    // Questi metodi vanno definiti nelle sottoclassi dao
    protected abstract String getInsertQuery();
    protected abstract void prepareInsert(PreparedStatement stmt, T entity) throws SQLException;

    protected abstract String getUpdateQuery();
    protected abstract void prepareUpdate(PreparedStatement stmt, T entity) throws SQLException;

    protected abstract void setGeneratedId(T entity, int id);

    protected abstract T mapRow(ResultSet rs) throws SQLException;
}
