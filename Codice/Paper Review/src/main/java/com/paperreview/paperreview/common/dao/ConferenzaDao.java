package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.UtenteEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

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
    // Metodo per associare un utente come chair a una conferenza
    public void addChairToConferenza(int utenteId, int conferenzaId) throws SQLException {
        String query = "INSERT INTO Chair (ref_utente, ref_conferenza) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, utenteId);
            stmt.setInt(2, conferenzaId);
            stmt.executeUpdate();
        }
    }

    // Metodo per ottenere tutti i chair (utenti) associati a una conferenza
    public Set<UtenteEntity> getChairsForConferenza(int conferenzaId) throws SQLException {
        String query = "SELECT u.id_utente, u.nome, u.cognome, u.email FROM Utente u " +
                "JOIN Chair c ON u.id_utente = c.ref_utente WHERE c.ref_conferenza = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, conferenzaId);
            ResultSet rs = stmt.executeQuery();
            Set<UtenteEntity> chairs = new HashSet<>();
            while (rs.next()) {
                UtenteEntity utente = new UtenteEntity(
                        rs.getInt("id_utente"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("email"),
                        null // Oltre alla password, se necessario
                );
                chairs.add(utente);
            }
            return chairs;
        }
    }
}