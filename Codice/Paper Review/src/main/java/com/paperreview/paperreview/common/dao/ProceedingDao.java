package com.paperreview.paperreview.common.DAO;

import com.paperreview.paperreview.entities.ProceedingEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProceedingDao extends BaseDao<ProceedingEntity> {

    public ProceedingDao(Connection connection) {
        super(connection, "Proceeding", "id_proceeding");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (titolo, data_sottomissione, ref_utente, ref_conferenza) " +
                "VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, ProceedingEntity proceeding) throws SQLException {
        stmt.setString(1, proceeding.getTitolo());
        stmt.setObject(2, proceeding.getDataSottomissione());
        stmt.setInt(3, proceeding.getRefUtente());
        stmt.setInt(4, proceeding.getRefConferenza());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET titolo = ?, data_sottomissione = ?, ref_utente = ?, ref_conferenza = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, ProceedingEntity proceeding) throws SQLException {
        stmt.setString(1, proceeding.getTitolo());
        stmt.setObject(2, proceeding.getDataSottomissione());
        stmt.setInt(3, proceeding.getRefUtente());
        stmt.setInt(4, proceeding.getRefConferenza());
        stmt.setInt(5, proceeding.getId());
    }

    @Override
    protected void setGeneratedId(ProceedingEntity proceeding, int id) {
        proceeding.setId(id);
    }

    @Override
    protected ProceedingEntity mapRow(ResultSet rs) throws SQLException {
        return new ProceedingEntity(
                rs.getInt("id_proceeding"),
                rs.getString("titolo"),
                rs.getObject("data_sottomissione", java.time.LocalDateTime.class),
                rs.getInt("ref_utente"),
                rs.getInt("ref_conferenza")
        );
    }
}
