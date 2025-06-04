package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.common.PasswordUtil;
import com.paperreview.paperreview.entities.UtenteEntity;
import com.paperreview.paperreview.entities.TopicEntity;

import java.sql.Connection;
import java.sql.*;
import java.util.Set;
import java.util.HashSet;

public class UtenteDao extends BaseDao<UtenteEntity> {

    public UtenteDao(Connection connection) {
        super(connection, "Utente", "id_utente");
    }

    @Override
    protected void setGeneratedId(UtenteEntity entity, int id) {
        entity.setId(id);
    }

    public UtenteEntity getByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null; // Nessun utente trovato con quell'email
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName +  " (nome, cognome, email, password) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, UtenteEntity u) throws SQLException {

        String hashedPassword = PasswordUtil.hashPassword(u.getPassword());

        stmt.setString(4, hashedPassword);
        u.setPassword(hashedPassword); // aggiorna l'entity

        stmt.setString(1, u.getNome());
        stmt.setString(2, u.getCognome());
        stmt.setString(3, u.getEmail());
        stmt.setString(4, hashedPassword);
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName +  " SET nome = ?, cognome = ?, email = ?, password = ? WHERE id_utente = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, UtenteEntity u) throws SQLException {
        stmt.setString(1, u.getNome());
        stmt.setString(2, u.getCognome());
        stmt.setString(3, u.getEmail());
        stmt.setString(4, u.getPassword());
        stmt.setInt(5, u.getId());
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM " + tableName +  " WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveIfNotExistsByEmail(UtenteEntity utente) throws SQLException, SQLSyntaxErrorException {

        try{
            if (emailExists(utente.getEmail()))
                return false;

            save(utente); // usa il metodo già presente
            return true;
        }catch(SQLException e){
            throw e;
        }

    }

    public UtenteEntity login(String email, String plainPassword) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");

                // Verifica la password
                if (PasswordUtil.verifyPassword(plainPassword, hashedPassword)) {
                    return new UtenteEntity(
                            rs.getInt("id_utente"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email"),
                            hashedPassword // conservi l'hash
                    );
                }
            }
        }

        return null; // login fallito
    }


    @Override
    protected UtenteEntity mapRow(ResultSet rs) throws SQLException {
        return new UtenteEntity(
                rs.getInt("id_utente"),
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("password")
        );
    }

    // Metodo per aggiungere un Topic all'Utente
    public void addTopicToUser(int utenteId, int topicId) throws SQLException {
        String query = "INSERT INTO TopicUtente (ref_utente, ref_topic) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, utenteId);
            stmt.setInt(2, topicId);
            stmt.executeUpdate();
        }
    }

    // Metodo per ottenere tutti i Topic associati a un Utente
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
}
