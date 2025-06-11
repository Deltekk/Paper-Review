package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.RuoloConferenzaEntity;
import com.paperreview.paperreview.entities.Ruolo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RuoloConferenzaDao extends BaseDao<RuoloConferenzaEntity> {

    public RuoloConferenzaDao(Connection connection) {
        super(connection, "Ruolo_conferenza", "id_ruolo");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (ruolo, ref_utente, ref_conferenza) VALUES (?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, RuoloConferenzaEntity entity) throws SQLException {
        stmt.setString(1, entity.getRuolo().toString()); // Enum Ruolo (usa .getValoreDb() se hai mapping custom)
        stmt.setInt(2, entity.getRefUtente());
        stmt.setInt(3, entity.getRefConferenza());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET ruolo = ?, ref_utente = ?, ref_conferenza = ? WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, RuoloConferenzaEntity entity) throws SQLException {
        stmt.setString(1, entity.getRuolo().toString());
        stmt.setInt(2, entity.getRefUtente());
        stmt.setInt(3, entity.getRefConferenza());
        stmt.setInt(4, entity.getId());
    }

    @Override
    protected void setGeneratedId(RuoloConferenzaEntity entity, int id) {
        entity.setId(id);
    }

    @Override
    protected RuoloConferenzaEntity mapRow(ResultSet rs) throws SQLException {
        return new RuoloConferenzaEntity(
                rs.getInt("id_ruolo"),
                Ruolo.fromString(rs.getString("ruolo")),
                rs.getInt("ref_utente"),
                rs.getInt("ref_conferenza")
        );
    }

    // Recupera tutti i ruoli di un utente per tutte le conferenze
    public List<RuoloConferenzaEntity> getByUtente(int refUtente) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_utente = ?";
        List<RuoloConferenzaEntity> results = new ArrayList<>();
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

    // Recupera tutti i ruoli associati a una conferenza
    public List<RuoloConferenzaEntity> getByConferenza(int refConferenza) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_conferenza = ?";
        List<RuoloConferenzaEntity> results = new ArrayList<>();
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

    // Recupera tutti i ruoli di un utente per una specifica conferenza
    public List<RuoloConferenzaEntity> getByUtenteAndConferenza(int refUtente, int refConferenza) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE ref_utente = ? AND ref_conferenza = ?";
        List<RuoloConferenzaEntity> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refUtente);
            stmt.setInt(2, refConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    public Set<Integer> getIdUtentiByRuoloAndConferenza(Ruolo ruolo, int refConferenza) throws SQLException {
        String query = "SELECT ref_utente FROM " + tableName + " WHERE ruolo = ? AND ref_conferenza = ?";
        Set<Integer> idUtenti = new HashSet<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, ruolo.toString());
            stmt.setInt(2, refConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    idUtenti.add(rs.getInt("ref_utente"));
                }
            }
        }
        return idUtenti;
    }
}