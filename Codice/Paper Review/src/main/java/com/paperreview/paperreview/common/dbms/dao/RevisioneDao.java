package com.paperreview.paperreview.common.dbms.dao;

import com.paperreview.paperreview.entities.RevisioneEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RevisioneDao extends BaseDao<RevisioneEntity> {

    public RevisioneDao(Connection connection) {
        super(connection, "Revisione", "id_revisione");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (ref_utente, ref_paper) VALUES (?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, RevisioneEntity revisione) throws SQLException {
        stmt.setInt(1, revisione.getRefUtente());
        stmt.setInt(2, revisione.getRefPaper());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET " +
                "testo = ?, " +
                "valutazione = ?, " +
                "data_sottomissione = ?, " +
                "commento_chair = ?, " +
                "punti_forza = ?, " +
                "punti_debolezza = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, RevisioneEntity revisione) throws SQLException {
        stmt.setString(1, revisione.getTesto());
        if (revisione.getValutazione() != 0) {
            stmt.setInt(2, revisione.getValutazione());
        } else {
            stmt.setNull(2, Types.INTEGER);
        }
        stmt.setObject(3, revisione.getDataSottomissione());
        stmt.setString(4, revisione.getCommentoChair());
        stmt.setString(5, revisione.getPuntiForza());
        stmt.setString(6, revisione.getPuntiDebolezza());
        stmt.setInt(7, revisione.getId());
    }

    @Override
    protected void setGeneratedId(RevisioneEntity revisione, int id) {
        revisione.setId(id);
    }

    @Override
    protected RevisioneEntity mapRow(ResultSet rs) throws SQLException {
        return new RevisioneEntity(
                rs.getInt("id_revisione"),
                rs.getString("testo"),
                rs.getObject("valutazione") != null ? rs.getInt("valutazione") : null,
                rs.getObject("data_sottomissione", java.time.LocalDateTime.class),
                rs.getString("punti_forza"),
                rs.getString("punti_debolezza"),
                rs.getString("commento_chair"),
                rs.getInt("ref_utente"),
                rs.getInt("ref_paper")
        );
    }

    public List<RevisioneEntity> getByPaper(int refPaper) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_paper = ?";
        List<RevisioneEntity> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refPaper);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    public List<RevisioneEntity> getByUtente(int refUtente) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_utente = ?";
        List<RevisioneEntity> results = new ArrayList<>();
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

    public double getAverageScore(int idPaper) throws SQLException {
        String query = "SELECT AVG(valutazione) FROM " + tableName + " WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaper);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    public void removeAllByPaper(int paperId) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, paperId);
            stmt.executeUpdate();
        }
    }
}