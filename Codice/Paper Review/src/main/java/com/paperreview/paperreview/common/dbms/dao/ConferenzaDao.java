package com.paperreview.paperreview.common.dbms.dao;

import com.paperreview.paperreview.entities.ConferenzaEntity;
import com.paperreview.paperreview.entities.MetodoAssegnazione;
import com.paperreview.paperreview.entities.MetodoValutazione;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConferenzaDao extends BaseDao<ConferenzaEntity> {

    public ConferenzaDao(Connection connection) {
        super(connection, "Conferenza", "id_conferenza");
    }

    @Override
    public List<ConferenzaEntity> getAll(){
        List<ConferenzaEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE data_conferenza > ? ORDER BY data_conferenza";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now())); // Imposta la data attuale
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs)); // Aggiungi ogni riga trovata
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<ConferenzaEntity> getAll(int ref_chair){
        List<ConferenzaEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE data_conferenza > ? AND id_conferenza IN (SELECT ref_conferenza FROM Ruolo_conferenza WHERE ruolo = 'Chair' AND ref_utente = ?) ORDER BY data_conferenza";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now())); // Imposta la data attuale
            stmt.setInt(2, ref_chair); // Imposta la data attuale
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs)); // Aggiungi ogni riga trovata
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<ConferenzaEntity> getAllIfNotAutore(int ref_autore){
        List<ConferenzaEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE data_conferenza > ? AND id_conferenza IN (SELECT ref_conferenza FROM Ruolo_conferenza WHERE ruolo != 'Autore' AND ref_utente = ?) ORDER BY data_conferenza";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now())); // Imposta la data attuale
            stmt.setInt(2, ref_autore); // Imposta la data attuale
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs)); // Aggiungi ogni riga trovata
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<ConferenzaEntity> getAllWhereAutore(int ref_autore){
        List<ConferenzaEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE data_conferenza > ? AND id_conferenza IN (SELECT ref_conferenza FROM Ruolo_conferenza WHERE ruolo = 'Autore' AND ref_utente = ?) ORDER BY data_conferenza";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now())); // Imposta la data attuale
            stmt.setInt(2, ref_autore); // Imposta la data attuale
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs)); // Aggiungi ogni riga trovata
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (nome, descrizione, data_conferenza, location, metodo_assegnazione, metodo_valutazione, paper_previsti, " +
                "rate_accettazione, giorni_preavviso, scadenza_sottomissione, scadenza_revisione, scadenza_sottomissione_2, scadenza_editing, " +
                "scadenza_sottomissione_3, scadenza_impaginazione) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }


    @Override
    protected void prepareInsert(PreparedStatement stmt, ConferenzaEntity conferenza) throws SQLException {
        stmt.setString(1, conferenza.getNome());
        stmt.setString(2, conferenza.getDescrizione());
        stmt.setObject(3, conferenza.getDataConferenza());
        stmt.setString(4, conferenza.getLocation());
        stmt.setString(5, conferenza.getMetodoAssegnazione().getValoreDb());
        stmt.setString(6, conferenza.getMetodoValutazione().getValoreDb());
        stmt.setInt(7, conferenza.getPaperPrevisti());
        stmt.setInt(8, conferenza.getRateAccettazione());
        stmt.setInt(9, conferenza.getGiorniPreavviso());
        stmt.setObject(10, conferenza.getScadenzaSottomissione());
        stmt.setObject(11, conferenza.getScadenzaRevisione());
        stmt.setObject(12, conferenza.getScadenzaSottomissione2());
        stmt.setObject(13, conferenza.getScadenzaEditing());
        stmt.setObject(14, conferenza.getScadenzaSottomissione3());
        stmt.setObject(15, conferenza.getScadenzaImpaginazione());
    }


    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET "
                + "nome = ?, "
                + "descrizione = ?, "
                + "data_conferenza = ?, "
                + "location = ?, "
                + "metodo_assegnazione = ?, "
                + "metodo_valutazione = ?, "
                + "rate_accettazione = ?, "      // nuovo
                + "paper_previsti = ?, "
                + "giorni_preavviso = ?, "       // nuovo
                + "scadenza_sottomissione = ?, "
                + "scadenza_revisione = ?, "
                + "scadenza_sottomissione_2 = ?, "
                + "scadenza_editing = ?, "
                + "scadenza_sottomissione_3 = ?, "
                + "scadenza_impaginazione = ? "
                + "WHERE " + idColumn + " = ?";
    }


    @Override
    protected void prepareUpdate(PreparedStatement stmt, ConferenzaEntity conferenza) throws SQLException {
        stmt.setString(1, conferenza.getNome());
        stmt.setString(2, conferenza.getDescrizione());
        stmt.setObject(3, conferenza.getDataConferenza());
        stmt.setString(4, conferenza.getLocation());
        stmt.setString(5, conferenza.getMetodoAssegnazione().getValoreDb());
        stmt.setString(6, conferenza.getMetodoValutazione().getValoreDb());
        stmt.setInt(7, conferenza.getRateAccettazione());
        stmt.setInt(8, conferenza.getPaperPrevisti());
        stmt.setInt(9, conferenza.getGiorniPreavviso());
        stmt.setObject(10, conferenza.getScadenzaSottomissione());
        stmt.setObject(11, conferenza.getScadenzaRevisione());
        stmt.setObject(12, conferenza.getScadenzaSottomissione2());
        stmt.setObject(13, conferenza.getScadenzaEditing());
        stmt.setObject(14, conferenza.getScadenzaSottomissione3());
        stmt.setObject(15, conferenza.getScadenzaImpaginazione());
        stmt.setInt(16, conferenza.getId());
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
                MetodoAssegnazione.fromString(rs.getString("metodo_assegnazione")),
                MetodoValutazione.fromString(rs.getString("metodo_valutazione")),
                rs.getInt("rate_accettazione"),                     // aggiunto
                rs.getInt("paper_previsti"),
                rs.getInt("giorni_preavviso"),                      // aggiunto
                rs.getObject("scadenza_sottomissione", java.time.LocalDateTime.class),
                rs.getObject("scadenza_revisione", java.time.LocalDateTime.class),
                rs.getObject("scadenza_sottomissione_2", java.time.LocalDateTime.class),
                rs.getObject("scadenza_editing", java.time.LocalDateTime.class),
                rs.getObject("scadenza_sottomissione_3", java.time.LocalDateTime.class),
                rs.getObject("scadenza_impaginazione", java.time.LocalDateTime.class)
        );
    }

}