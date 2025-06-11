package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.PaperEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaperDao extends BaseDao<PaperEntity> {

    public PaperDao(Connection connection) {
        super(connection, "Paper", "id_paper");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (titolo, abstract, file, data_sottomissione, ref_utente, ref_conferenza) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, PaperEntity paper) throws SQLException {
        stmt.setString(1, paper.getTitolo());
        stmt.setString(2, paper.getAbstractPaper());    // Può essere null
        stmt.setBytes(3, paper.getFile());              // BLOB
        stmt.setObject(4, paper.getDataSottomissione());
        stmt.setInt(5, paper.getRefUtente());
        stmt.setInt(6, paper.getRefConferenza());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET titolo = ?, abstract = ?, file = ?, data_sottomissione = ?, ref_utente = ?, ref_conferenza = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, PaperEntity paper) throws SQLException {
        stmt.setString(1, paper.getTitolo());
        stmt.setString(2, paper.getAbstractPaper());
        stmt.setBytes(3, paper.getFile());
        stmt.setObject(4, paper.getDataSottomissione());
        stmt.setInt(5, paper.getRefUtente());
        stmt.setInt(6, paper.getRefConferenza());
        stmt.setInt(7, paper.getId());
    }

    @Override
    protected void setGeneratedId(PaperEntity paper, int id) {
        paper.setId(id);
    }

    @Override
    protected PaperEntity mapRow(ResultSet rs) throws SQLException {
        return new PaperEntity(
                rs.getInt("id_paper"),
                rs.getString("titolo"),
                rs.getString("abstract"),
                rs.getBytes("file"),
                rs.getObject("data_sottomissione", java.time.LocalDateTime.class),
                rs.getInt("ref_utente"),
                rs.getInt("ref_conferenza")
        );
    }

    // Recupera tutti i paper di un utente
    public List<PaperEntity> getPapersByUtente(int refUtente) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_utente = ?";
        List<PaperEntity> results = new ArrayList<>();
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

    // Recupera tutti i paper per una conferenza
    public List<PaperEntity> getPapersByConferenza(int refConferenza) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_conferenza = ?";
        List<PaperEntity> results = new ArrayList<>();
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
}