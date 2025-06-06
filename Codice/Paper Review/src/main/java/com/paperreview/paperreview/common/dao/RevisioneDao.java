package com.paperreview.paperreview.common.DAO;

import com.paperreview.paperreview.entities.RevisioneEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RevisioneDao extends BaseDao<RevisioneEntity> {

    public RevisioneDao(Connection connection) {
        super(connection, "Revisione", "id_revisione");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (testo, valutazione, data_sottomissione, ref_utente, ref_paper) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, RevisioneEntity revisione) throws SQLException {
        stmt.setString(1, revisione.getTesto());
        stmt.setInt(2, revisione.getValutazione());
        stmt.setObject(3, revisione.getDataSottomissione());
        stmt.setInt(4, revisione.getRefUtente());
        stmt.setInt(5, revisione.getRefPaper());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET testo = ?, valutazione = ?, data_sottomissione = ?, ref_utente = ?, ref_paper = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, RevisioneEntity revisione) throws SQLException {
        stmt.setString(1, revisione.getTesto());
        stmt.setInt(2, revisione.getValutazione());
        stmt.setObject(3, revisione.getDataSottomissione());
        stmt.setInt(4, revisione.getRefUtente());
        stmt.setInt(5, revisione.getRefPaper());
        stmt.setInt(6, revisione.getId());
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
                rs.getInt("valutazione"),
                rs.getObject("data_sottomissione", java.time.LocalDateTime.class),
                rs.getInt("ref_utente"),
                rs.getInt("ref_paper")
        );
    }
}