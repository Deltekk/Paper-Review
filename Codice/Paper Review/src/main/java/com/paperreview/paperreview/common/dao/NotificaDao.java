package com.paperreview.paperreview.common.DAO;

import com.paperreview.paperreview.entities.NotificaEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificaDao extends BaseDao<NotificaEntity> {

    public NotificaDao(Connection connection) {
        super(connection, "Notifica", "id_notifica");
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
