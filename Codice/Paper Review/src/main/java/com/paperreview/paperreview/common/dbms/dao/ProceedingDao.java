package com.paperreview.paperreview.common.dbms.dao;

import com.paperreview.paperreview.entities.ProceedingEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProceedingDao extends BaseDao<ProceedingEntity> {

    public ProceedingDao(Connection connection) {
        super(connection, "Proceeding", "id_proceeding");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (titolo, file, data_sottomissione, ref_utente, ref_conferenza) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, ProceedingEntity proceeding) throws SQLException {
        stmt.setString(1, proceeding.getTitolo());

        if (proceeding.getFile() != null) {
            stmt.setBytes(2, proceeding.getFile());
        } else {
            stmt.setNull(2, Types.BLOB);
        }

        if (proceeding.getDataSottomissione() != null) {
            stmt.setObject(3, proceeding.getDataSottomissione());
        } else {
            stmt.setNull(3, Types.TIMESTAMP);
        }

        stmt.setInt(4, proceeding.getRefUtente());
        stmt.setInt(5, proceeding.getRefConferenza());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET titolo = ?, file = ?, data_sottomissione = ?, ref_utente = ?, ref_conferenza = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, ProceedingEntity proceeding) throws SQLException {
        stmt.setString(1, proceeding.getTitolo());

        if (proceeding.getFile() != null) {
            stmt.setBytes(2, proceeding.getFile());
        } else {
            stmt.setNull(2, Types.BLOB);
        }

        if (proceeding.getDataSottomissione() != null) {
            stmt.setObject(3, proceeding.getDataSottomissione());
        } else {
            stmt.setNull(3, Types.TIMESTAMP);
        }

        stmt.setInt(4, proceeding.getRefUtente());
        stmt.setInt(5, proceeding.getRefConferenza());
        stmt.setInt(6, proceeding.getId());
    }

    @Override
    protected void setGeneratedId(ProceedingEntity proceeding, int id) {
        proceeding.setId(id);
    }

    @Override
    protected ProceedingEntity mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_proceeding");
        String titolo = rs.getString("titolo");
        byte[] file = rs.getBytes("file");
        LocalDateTime data = rs.getObject("data_sottomissione", LocalDateTime.class);
        int refUtente = rs.getInt("ref_utente");
        int refConferenza = rs.getInt("ref_conferenza");

        return new ProceedingEntity(id, titolo, file, data, refUtente, refConferenza);
    }

    public List<ProceedingEntity> getByUtente(int refUtente) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_utente = ?";
        List<ProceedingEntity> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    public List<ProceedingEntity> getByConferenza(int refConferenza) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_conferenza = ?";
        List<ProceedingEntity> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    public List<byte[]> getFileConferenza(int refConferenza) throws SQLException {
        String query = "SELECT file FROM " + tableName + " WHERE ref_conferenza = ?";
        List<byte[]> files = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    files.add(rs.getBytes("file"));
                }
            }
        }
        return files;
    }
}