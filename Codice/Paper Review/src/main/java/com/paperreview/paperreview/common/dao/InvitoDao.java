package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.InvitoStatusEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class InvitoDao extends BaseDao<InvitoEntity> {

    public InvitoDao(Connection connection) {
        super(connection, "Invito", "id_invito");
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName + " (data, testo, status, email, codice, ref_conferenza, ref_mittente, ref_destinatario) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, InvitoEntity invito) throws SQLException {
        stmt.setObject(1, invito.getData());  // LocalDateTime
        stmt.setString(2, invito.getTesto());
        stmt.setString(3, invito.getStatus().getStatus());  // Usa il metodo getStatus() dell'Enum
        stmt.setString(4, invito.getEmail());
        stmt.setString(5, invito.getCodice());
        stmt.setInt(6, invito.getRefConferenza());
        stmt.setInt(7, invito.getRefMittente());
        stmt.setObject(8, invito.getRefDestinatario());  // Integer (può essere null)
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName + " SET data = ?, testo = ?, status = ?, email = ?, codice = ?, ref_conferenza = ?, ref_mittente = ?, ref_destinatario = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, InvitoEntity invito) throws SQLException {
        stmt.setObject(1, invito.getData());  // LocalDateTime
        stmt.setString(2, invito.getTesto());
        stmt.setString(3, invito.getStatus().getStatus());  // Usa il metodo getStatus() dell'Enum
        stmt.setString(4, invito.getEmail());
        stmt.setString(5, invito.getCodice());
        stmt.setInt(6, invito.getRefConferenza());
        stmt.setInt(7, invito.getRefMittente());
        stmt.setObject(8, invito.getRefDestinatario());  // Integer (può essere null)
        stmt.setInt(9, invito.getId());  // Aggiungi l'ID per il WHERE
    }

    @Override
    protected void setGeneratedId(InvitoEntity invito, int id) {
        invito.setId(id);
    }

    @Override
    protected InvitoEntity mapRow(ResultSet rs) throws SQLException {
        return new InvitoEntity(
                rs.getInt("id_invito"),
                rs.getObject("data", java.time.LocalDateTime.class),
                rs.getString("testo"),
                InvitoStatusEnum.fromString(rs.getString("status")),  // Converti la stringa in un enum
                rs.getString("email"),
                rs.getString("codice"),
                rs.getInt("ref_conferenza"),
                rs.getInt("ref_mittente"),
                (Integer) rs.getObject("ref_destinatario")  // Gestisci il valore null per ref_destinatario
        );
    }

    public String acceptInvito(String codice) {
        String message = "";

        // 1. Verifica l'esistenza dell'invito
        InvitoEntity invito = getInvitoByCodice(codice);  // Metodo che recupera l'invito tramite codice

        if (invito == null) {
            // Se l'invito non esiste
            message = "Inesistente";
        } else {
            // 2. Verifica se l'invito è ancora valido
            if (isInvitoScaduto(invito)) {
                // Se l'invito è scaduto
                message = "Scaduto";
            } else {
                // 3. Aggiorna lo stato dell'invito
                invito.setStatus(InvitoStatusEnum.ACCEPTED);  // Imposta lo status su "Accettato"
                updateInvito(invito);  // Metodo che aggiorna la tabella con il nuovo status

                message = "Accettato";
            }
        }

        return message;
    }

    private InvitoEntity getInvitoByCodice(String codice) {
        // Esegui una query per ottenere l'invito in base al codice
        String query = "SELECT * FROM Invito WHERE codice = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, codice);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);  // Mappa il risultato in un InvitoEntity
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Se non trova l'invito
    }

    private boolean isInvitoScaduto(InvitoEntity invito) {
        // Controlla se l'invito è scaduto. Puoi confrontare la data con l'ora attuale
        LocalDateTime now = LocalDateTime.now();
        return invito.getData().isBefore(now);  // Restituisce true se la data dell'invito è passata
    }

    private void updateInvito(InvitoEntity invito) {
        // Esegui l'update per modificare lo stato dell'invito
        String query = "UPDATE Invito SET status = ? WHERE id_invito = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, invito.getStatus().getStatus());  // Setta lo status su "Accettato"
            stmt.setInt(2, invito.getId());  // Usa l'id dell'invito per l'UPDATE
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}