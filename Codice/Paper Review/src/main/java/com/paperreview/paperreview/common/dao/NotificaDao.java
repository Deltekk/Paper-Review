package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.InvitoStatusEnum;
import com.paperreview.paperreview.entities.NotificaEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificaDao extends BaseDao<NotificaEntity> {

    public NotificaDao(Connection connection) {
        super(connection, "Notifica", "id_notifica");
    }

    @Override
    public List<NotificaEntity> getAll(){
        List<NotificaEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE isLetta = false";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<NotificaEntity> getAllArchived() {
        List<NotificaEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE isLetta = true";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (data, testo, isLetta, ref_utente, ref_conferenza) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, NotificaEntity notifica) throws SQLException {
        stmt.setObject(1, notifica.getData());  // LocalDateTime
        stmt.setString(2, notifica.getTesto());
        stmt.setBoolean(3, notifica.isLetta());
        stmt.setInt(4, notifica.getRefUtente());
        stmt.setInt(5, notifica.getRefConferenza());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET data = ?, testo = ?, isLetta = ?, ref_utente = ?, ref_conferenza = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, NotificaEntity notifica) throws SQLException {
        stmt.setObject(1, notifica.getData());  // LocalDateTime
        stmt.setString(2, notifica.getTesto());
        stmt.setBoolean(3, notifica.isLetta());
        stmt.setInt(4, notifica.getRefUtente());
        stmt.setInt(5, notifica.getRefConferenza());
        stmt.setInt(6, notifica.getId());  // Aggiungi l'ID per il WHERE
    }

    @Override
    protected void setGeneratedId(NotificaEntity notifica, int id) {
        notifica.setId(id);
    }

    @Override
    protected NotificaEntity mapRow(ResultSet rs) throws SQLException {
        return new NotificaEntity(
                rs.getInt("id_notifica"),
                rs.getObject("data", java.time.LocalDateTime.class),
                rs.getString("testo"),
                rs.getBoolean("isLetta"),
                rs.getInt("ref_utente"),
                rs.getInt("ref_conferenza")
        );
    }
}
