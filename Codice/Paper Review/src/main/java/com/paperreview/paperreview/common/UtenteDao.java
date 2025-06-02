package com.paperreview.paperreview.common;

import com.paperreview.paperreview.entities.UtenteEntity;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.*;
import java.util.List;

public class UtenteDao extends BaseDao<UtenteEntity> {

    public UtenteDao(Connection connection) {
        super(connection, "Utente", "id_utente");
    }

    @Override
    protected void setGeneratedId(UtenteEntity entity, int id) {
        entity.setIdUtente(id);
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName +  " (nome, cognome, email, password) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, UtenteEntity u) throws SQLException {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, u.getPassword().toCharArray());

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
                if (BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified) {
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
}
