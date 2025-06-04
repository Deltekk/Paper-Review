package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.PaperEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaperDao extends BaseDao<PaperEntity> {

    public PaperDao(Connection connection) {
        super(connection, "Paper", "id_paper");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (titolo, contenuto, data_sottomissione, ref_utente, ref_conferenza) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, PaperEntity paper) throws SQLException {
        stmt.setString(1, paper.getTitolo());
        stmt.setString(2, paper.getContenuto());
        stmt.setObject(3, paper.getDataSottomissione());
        stmt.setInt(4, paper.getRefUtente());
        stmt.setInt(5, paper.getRefConferenza());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET titolo = ?, contenuto = ?, data_sottomissione = ?, ref_utente = ?, ref_conferenza = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, PaperEntity paper) throws SQLException {
        stmt.setString(1, paper.getTitolo());
        stmt.setString(2, paper.getContenuto());
        stmt.setObject(3, paper.getDataSottomissione());
        stmt.setInt(4, paper.getRefUtente());
        stmt.setInt(5, paper.getRefConferenza());
        stmt.setInt(6, paper.getId());
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
                rs.getString("contenuto"),
                rs.getObject("data_sottomissione", java.time.LocalDateTime.class),
                rs.getInt("ref_utente"),
                rs.getInt("ref_conferenza")
        );
    }
}
