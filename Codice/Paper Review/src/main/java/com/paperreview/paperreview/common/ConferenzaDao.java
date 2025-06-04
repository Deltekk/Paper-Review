package com.paperreview.paperreview.common;

import com.paperreview.paperreview.entities.ConferenzaEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConferenzaDao extends BaseDao<ConferenzaEntity> {

    public ConferenzaDao(Connection connection) {
        super(connection, "Conferenza", "id_conferenza");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (nome, descrizione, data_conferenza, location, metodo_assegnazione, metodo_valutazione, paper_previsti, scadenza_sottomissione, " +
                "scadenza_revisione, scadenza_sottomissione_2, scadenza_editing, scadenza_sottomissione_3, scadenza_impaginazione) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, ConferenzaEntity conferenza) throws SQLException {
        stmt.setString(1, conferenza.getNome());
        stmt.setString(2, conferenza.getDescrizione());
        stmt.setObject(3, conferenza.getDataConferenza());
        stmt.setString(4, conferenza.getLocation());
        stmt.setString(5, conferenza.getMetodoAssegnazione());
        stmt.setString(6, conferenza.getMetodoValutazione());
        stmt.setInt(7, conferenza.getPaperPrevisti());
        stmt.setObject(8, conferenza.getScadenzaSottomissione());
        stmt.setObject(9, conferenza.getScadenzaRevisione());
        stmt.setObject(10, conferenza.getScadenzaSottomissione2());
        stmt.setObject(11, conferenza.getScadenzaEditing());
        stmt.setObject(12, conferenza.getScadenzaSottomissione3());
        stmt.setObject(13, conferenza.getScadenzaImpaginazione());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET nome = ?, descrizione = ?, data_conferenza = ?, location = ?, metodo_assegnazione = ?, metodo_valutazione = ?, paper_previsti = ?, " +
                "scadenza_sottomissione = ?, scadenza_revisione = ?, scadenza_sottomissione_2 = ?, scadenza_editing = ?, scadenza_sottomissione_3 = ?, scadenza_impaginazione = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, ConferenzaEntity conferenza) throws SQLException {
        stmt.setString(1, conferenza.getNome());
        stmt.setString(2, conferenza.getDescrizione());
        stmt.setObject(3, conferenza.getDataConferenza());
        stmt.setString(4, conferenza.getLocation());
        stmt.setString(5, conferenza.getMetodoAssegnazione());
        stmt.setString(6, conferenza.getMetodoValutazione());
        stmt.setInt(7, conferenza.getPaperPrevisti());
        stmt.setObject(8, conferenza.getScadenzaSottomissione());
        stmt.setObject(9, conferenza.getScadenzaRevisione());
        stmt.setObject(10, conferenza.getScadenzaSottomissione2());
        stmt.setObject(11, conferenza.getScadenzaEditing());
        stmt.setObject(12, conferenza.getScadenzaSottomissione3());
        stmt.setObject(13, conferenza.getScadenzaImpaginazione());
        stmt.setInt(14, conferenza.getId());
    }

    @Override
    protected void setGeneratedId(ConferenzaEntity conferenza, int id) {
        conferenza.setId(id);
    }

    @Override
    protected ConferenzaEntity mapRow(ResultSet rs) throws SQLException {
        return new ConferenzaEntity(
                rs.getInt("id_conferenza"),
                rs.getString("nome"),
                rs.getString("descrizione"),
                rs.getObject("data_conferenza", java.time.LocalDateTime.class),
                rs.getString("location"),
                rs.getString("metodo_assegnazione"),
                rs.getString("metodo_valutazione"),
                rs.getInt("paper_previsti"),
                rs.getObject("scadenza_sottomissione", java.time.LocalDateTime.class),
                rs.getObject("scadenza_revisione", java.time.LocalDateTime.class),
                rs.getObject("scadenza_sottomissione_2", java.time.LocalDateTime.class),
                rs.getObject("scadenza_editing", java.time.LocalDateTime.class),
                rs.getObject("scadenza_sottomissione_3", java.time.LocalDateTime.class),
                rs.getObject("scadenza_impaginazione", java.time.LocalDateTime.class)
        );
    }
}
