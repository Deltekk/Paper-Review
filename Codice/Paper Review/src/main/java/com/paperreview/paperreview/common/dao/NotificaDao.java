package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.NotificaEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificaDao extends BaseDao<NotificaEntity> {

    public NotificaDao(Connection connection) {
        super(connection, "Notifica", "id_notifica");
    }


    // Notifiche NON lette per uno specifico utente
    public List<NotificaEntity> getAll(int refUtente) {
        List<NotificaEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE isLetta = false AND ref_utente = ? ORDER BY data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Notifiche LETTE (archiviate) per uno specifico utente
    public List<NotificaEntity> getAllArchived(int refUtente) {
        List<NotificaEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE isLetta = true AND ref_utente = ? ORDER BY data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Segna una notifica come letta
    public boolean segnaComeLetta(int idNotifica) {
        String query = "UPDATE " + tableName + " SET isLetta = true WHERE id_notifica = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idNotifica);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
