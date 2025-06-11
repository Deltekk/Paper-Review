package com.paperreview.paperreview.common.dao;

import com.paperreview.paperreview.entities.InvitoEntity;
import com.paperreview.paperreview.entities.NotificaEntity;
import com.paperreview.paperreview.entities.StatusInvito;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvitoDao extends BaseDao<InvitoEntity> {

    public InvitoDao(Connection connection) {
        super(connection, "Invito", "id_invito");
    }

    @Override
    public List<InvitoEntity> getAll(){
        List<InvitoEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE status = ? AND data > ? ORDER BY data";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, StatusInvito.Inviato.toString());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Imposta la data attuale
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

    public List<InvitoEntity> getAllArchived() {
        List<InvitoEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE status != ? OR data < ? ORDER BY data";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, StatusInvito.Inviato.toString());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // Imposta la data attuale
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
        return "INSERT INTO " + tableName + " (data, testo, status, email, codice, ref_conferenza, ref_mittente, ref_destinatario) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, InvitoEntity invito) throws SQLException {
        stmt.setObject(1, invito.getData());  // LocalDateTime
        stmt.setString(2, invito.getTesto());
        stmt.setString(3, invito.getStatus().toString());  // Usa il metodo getStatus() dell'Enum
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
        stmt.setString(3, invito.getStatus().toString());  // Usa il metodo getStatus() dell'Enum
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
                StatusInvito.fromString(rs.getString("status")),  // Converti la stringa in un enum
                rs.getString("email"),
                rs.getString("codice"),
                rs.getInt("ref_conferenza"),
                rs.getInt("ref_mittente"),
                (Integer) rs.getObject("ref_destinatario")  // Gestisci il valore null per ref_destinatario
        );
    }

    public String acceptOrRejectInvito(String codice, boolean accept) {
        InvitoEntity invito = getInvitoByCodice(codice);
        if (invito == null) return "Inesistente";
        if (invito.getStatus() == StatusInvito.Accettato) return "Già accettato";
        if (invito.getStatus() == StatusInvito.Rifiutato) return "Già rifiutato";
        if (isInvitoScaduto(invito)) return "Scaduto";
        invito.setStatus(accept ? StatusInvito.Accettato : StatusInvito.Rifiutato);
        updateInvito(invito);
        return accept ? "Accettato" : "Rifiutato";
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
            stmt.setString(1, invito.getStatus().toString());  // Setta lo status su "Accettato"
            stmt.setInt(2, invito.getId());  // Usa l'id dell'invito per l'UPDATE
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<InvitoEntity> getAllToRead(int refDestinatario) {
        List<InvitoEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName +
                " WHERE ref_destinatario = ? AND status = ? AND data > ? ORDER BY data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refDestinatario);
            stmt.setString(2, StatusInvito.Inviato.toString());
            stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<InvitoEntity> getAllArchived(int refDestinatario) {
        List<InvitoEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName +
                " WHERE ref_destinatario = ? AND " +
                "(" +
                "status IN (?, ?) OR " +                 // Accettato o Rifiutato
                "(status = ? AND data <= ?)" +           // Oppure scaduto ma ancora Inviato
                ")" +
                " ORDER BY data DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refDestinatario);
            stmt.setString(2, StatusInvito.Accettato.toString());
            stmt.setString(3, StatusInvito.Rifiutato.toString());
            stmt.setString(4, StatusInvito.Inviato.toString());
            stmt.setTimestamp(5, Timestamp.valueOf(java.time.LocalDateTime.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<InvitoEntity> getAllToReadByEmail(String email) {
        List<InvitoEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName +
                " WHERE email = ? AND status = ? AND data > ? ORDER BY data DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, StatusInvito.Inviato.toString());
            stmt.setTimestamp(3, Timestamp.valueOf(java.time.LocalDateTime.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}