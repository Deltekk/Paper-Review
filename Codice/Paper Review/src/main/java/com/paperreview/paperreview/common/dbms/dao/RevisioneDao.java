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
        return "INSERT INTO " + tableName + " (ref_utente, ref_paper, ref_sottorevisore) VALUES (?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, RevisioneEntity revisione) throws SQLException {
        stmt.setInt(1, revisione.getRefUtente());
        stmt.setInt(2, revisione.getRefPaper());
        if (revisione.getRefSottorevisore() != null) {
            stmt.setInt(3, revisione.getRefSottorevisore());
        } else {
            stmt.setNull(3, Types.INTEGER);
        }
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET " +
                "testo = ?, valutazione = ?, data_sottomissione = ?, " +
                "commento_chair = ?, punti_forza = ?, punti_debolezza = ?, " +
                "ref_sottorevisore = ? WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, RevisioneEntity revisione) throws SQLException {
        stmt.setString(1, revisione.getTesto());
        if (revisione.getValutazione() != null)
            stmt.setInt(2, revisione.getValutazione());
        else
            stmt.setNull(2, Types.INTEGER);

        stmt.setObject(3, revisione.getDataSottomissione());
        stmt.setString(4, revisione.getCommentoChair());
        stmt.setString(5, revisione.getPuntiForza());
        stmt.setString(6, revisione.getPuntiDebolezza());

        if (revisione.getRefSottorevisore() != null)
            stmt.setInt(7, revisione.getRefSottorevisore());
        else
            stmt.setNull(7, Types.INTEGER);

        stmt.setInt(8, revisione.getId());
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
                rs.getInt("ref_paper"),
                (Integer) rs.getObject("ref_sottorevisore")
        );
    }

    public List<RevisioneEntity> getByPaper(int refPaper) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_paper = ?";
        List<RevisioneEntity> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refPaper);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
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
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    public List<RevisioneEntity> getByConferenza(int refConferenza) throws SQLException {
        String query = "SELECT R.* FROM " + tableName + " R JOIN Paper P ON R.ref_paper = P.id_paper WHERE P.ref_conferenza = ?";
        List<RevisioneEntity> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    public List<RevisioneEntity> getByUtenteAndConferenza(int refUtente, int refConferenza) throws SQLException {
        String query = """
        SELECT R.* FROM Revisione R
        JOIN Paper P ON R.ref_paper = P.id_paper
        WHERE R.ref_utente = ? AND P.ref_conferenza = ?
        """;
        List<RevisioneEntity> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refUtente);
            stmt.setInt(2, refConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        }
        return results;
    }

    public double getAverageScore(int idPaper) throws SQLException {
        String query = "SELECT AVG(valutazione) FROM " + tableName + " WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaper);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
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

    public int countRevisioniByUtenteAndConferenza(int idUtente, int idConferenza) throws SQLException {
        String query = """
        SELECT COUNT(*) FROM Revisione R
        JOIN Paper P ON R.ref_paper = P.id_paper
        WHERE R.ref_utente = ? AND P.ref_conferenza = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUtente);
            stmt.setInt(2, idConferenza);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public int countRevisioniByPaper(int idPaper) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaper);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    public RevisioneEntity getByUtenteAndPaper(int refUtente, int refPaper) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_utente = ? AND ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refUtente);
            stmt.setInt(2, refPaper);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public void removeById(int id) {
        String query = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}