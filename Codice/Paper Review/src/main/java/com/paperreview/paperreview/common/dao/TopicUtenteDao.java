package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.TopicEntity;
import com.paperreview.paperreview.entities.UtenteEntity;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * DAO per la relazione TopicUtente (molti-a-molti tra Utente e Topic)
 */
public class TopicUtenteDao {

    private final Connection connection;

    public TopicUtenteDao(Connection connection) {
        this.connection = connection;
    }

    // Associa un topic a un utente
    public void addTopicToUser(int utenteId, int topicId) throws SQLException {
        String query = "INSERT IGNORE INTO TopicUtente (ref_utente, ref_topic) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, utenteId);
            stmt.setInt(2, topicId);
            stmt.executeUpdate();
        }
    }

    // Rimuove l'associazione tra un utente e un topic
    public void removeTopicFromUser(int utenteId, int topicId) throws SQLException {
        String query = "DELETE FROM TopicUtente WHERE ref_utente = ? AND ref_topic = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, utenteId);
            stmt.setInt(2, topicId);
            stmt.executeUpdate();
        }
    }

    // Ottiene tutti i Topic associati a un utente
    public Set<TopicEntity> getTopicsForUser(int utenteId) throws SQLException {
        String query = "SELECT t.id_topic, t.nome FROM Topic t " +
                "JOIN TopicUtente tu ON t.id_topic = tu.ref_topic WHERE tu.ref_utente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, utenteId);
            ResultSet rs = stmt.executeQuery();
            Set<TopicEntity> topics = new HashSet<>();
            while (rs.next()) {
                TopicEntity topic = new TopicEntity();
                topic.setId(rs.getInt("id_topic"));
                topic.setNome(rs.getString("nome"));
                topics.add(topic);
            }
            return topics;
        }
    }

    // Ottiene tutti gli Utenti associati a un topic
    public Set<UtenteEntity> getUsersForTopic(int topicId) throws SQLException {
        String query = "SELECT u.id_utente, u.nome, u.cognome, u.email, u.password FROM Utente u " +
                "JOIN TopicUtente tu ON u.id_utente = tu.ref_utente WHERE tu.ref_topic = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, topicId);
            ResultSet rs = stmt.executeQuery();
            Set<UtenteEntity> utenti = new HashSet<>();
            while (rs.next()) {
                UtenteEntity utente = new UtenteEntity(
                    rs.getInt("id_utente"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("password"));
                utenti.add(utente);
            }
            return utenti;
        }
    }
}
