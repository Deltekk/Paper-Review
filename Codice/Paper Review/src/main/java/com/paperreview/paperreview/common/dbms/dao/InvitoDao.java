package com.paperreview.paperreview.common.dbms.dao;

import com.paperreview.paperreview.entities.InvitoEntity;
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
    protected String getInsertQuery() {
        return "INSERT INTO " + tableName +
                " (data, testo, status, email, codice, ref_conferenza, ref_mittente, ref_destinatario, ref_paper) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected void prepareInsert(PreparedStatement stmt, InvitoEntity invito) throws SQLException {
        stmt.setObject(1, invito.getData());
        stmt.setString(2, invito.getRuolo());
        stmt.setString(3, invito.getStatus().toString());
        stmt.setString(4, invito.getEmail());
        stmt.setString(5, invito.getCodice());
        stmt.setInt(6, invito.getRefConferenza());
        stmt.setInt(7, invito.getRefMittente());
        stmt.setObject(8, invito.getRefDestinatario());
        stmt.setObject(9, invito.getRefPaper());
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE " + tableName +
                " SET data = ?, testo = ?, status = ?, email = ?, codice = ?, " +
                "ref_conferenza = ?, ref_mittente = ?, ref_destinatario = ?, ref_paper = ? " +
                "WHERE " + idColumn + " = ?";
    }

    @Override
    protected void prepareUpdate(PreparedStatement stmt, InvitoEntity invito) throws SQLException {
        stmt.setObject(1, invito.getData());
        stmt.setString(2, invito.getRuolo());
        stmt.setString(3, invito.getStatus().toString());
        stmt.setString(4, invito.getEmail());
        stmt.setString(5, invito.getCodice());
        stmt.setInt(6, invito.getRefConferenza());
        stmt.setInt(7, invito.getRefMittente());
        stmt.setObject(8, invito.getRefDestinatario());
        stmt.setObject(9, invito.getRefPaper());
        stmt.setInt(10, invito.getId());
    }

    @Override
    protected void setGeneratedId(InvitoEntity invito, int id) {
        invito.setId(id);
    }

    @Override
    protected InvitoEntity mapRow(ResultSet rs) throws SQLException {
        return new InvitoEntity(
                rs.getInt("id_invito"),
                rs.getObject("data", LocalDateTime.class),
                rs.getString("testo"),
                StatusInvito.fromString(rs.getString("status")),
                rs.getString("email"),
                rs.getString("codice"),
                rs.getInt("ref_conferenza"),
                rs.getInt("ref_mittente"),
                (Integer) rs.getObject("ref_destinatario"),
                (Integer) rs.getObject("ref_paper")
        );
    }

    public InvitoEntity getInvitoByCodice(String codice) {
        String query = "SELECT * FROM Invito WHERE codice = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, codice);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String acceptOrRejectInvito(String codice, boolean accept) {
        InvitoEntity invito = getInvitoByCodice(codice);
        if (invito == null) return "Inesistente";
        if (isInvitoScaduto(invito)) return "Scaduto";
        invito.setStatus(accept ? StatusInvito.Accettato : StatusInvito.Rifiutato);
        updateInvito(invito);
        return accept ? "Accettato" : "Rifiutato";
    }

    private boolean isInvitoScaduto(InvitoEntity invito) {
        return invito.getData().isBefore(LocalDateTime.now());
    }

    private void updateInvito(InvitoEntity invito) {
        String query = "UPDATE Invito SET status = ? WHERE id_invito = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, invito.getStatus().toString());
            stmt.setInt(2, invito.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<InvitoEntity> getAll(int refUtente) {
        List<InvitoEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName +
                " WHERE status = ? AND data > ? AND ref_destinatario = ? ORDER BY data";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, StatusInvito.Inviato.toString());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, refUtente);
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

    public List<InvitoEntity> getAllArchived() {
        List<InvitoEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName +
                " WHERE status != ? OR data < ? ORDER BY data";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, StatusInvito.Inviato.toString());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
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

    public List<InvitoEntity> getAllToRead(int refDestinatario) {
        List<InvitoEntity> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName +
                " WHERE ref_destinatario = ? AND status = ? AND data > ? ORDER BY data DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refDestinatario);
            stmt.setString(2, StatusInvito.Inviato.toString());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
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
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
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
                " WHERE ref_destinatario = ? AND (" +
                "status IN (?, ?) OR (status = ? AND data <= ?)) " +
                "ORDER BY data DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, refDestinatario);
            stmt.setString(2, StatusInvito.Accettato.toString());
            stmt.setString(3, StatusInvito.Rifiutato.toString());
            stmt.setString(4, StatusInvito.Inviato.toString());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
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