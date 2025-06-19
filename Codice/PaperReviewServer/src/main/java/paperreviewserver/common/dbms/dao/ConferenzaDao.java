package paperreviewserver.common.dbms.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ConferenzaDao {
    private final Connection connection;

    public ConferenzaDao(Connection connection) {
        this.connection = connection;
    }

    public Map<Integer, String> getAllIdsAndNomi() throws SQLException {
        Map<Integer, String> result = new HashMap<>();
        String query = "SELECT id_conferenza, nome FROM Conferenza";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getInt("id_conferenza"), rs.getString("nome"));
            }
        }
        return result;
    }

    public Integer getRateAccettazione(int idConferenza) throws SQLException {
        String q = "SELECT rate_accettazione FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("rate_accettazione");
                }
            }
        }
        return null;
    }

    public Integer getGiorniPreavviso(int idConferenza) throws SQLException {
        String q = "SELECT giorni_preavviso FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("giorni_preavviso");
                }
            }
        }
        return null;
    }

    public LocalDateTime getScadenzaSottomissione(int idConferenza) throws SQLException {
        String q = "SELECT scadenza_sottomissione FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getTimestamp(1) != null)
                    return rs.getTimestamp(1).toLocalDateTime();
                return null;
            }
        }
    }

    public LocalDateTime getScadenzaRevisione(int idConferenza) throws SQLException {
        String q = "SELECT scadenza_revisione FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getTimestamp(1) != null)
                    return rs.getTimestamp(1).toLocalDateTime();
                return null;
            }
        }
    }

    public LocalDateTime getScadenzaSottomissione2(int idConferenza) throws SQLException {
        String q = "SELECT scadenza_sottomissione_2 FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getTimestamp(1) != null)
                    return rs.getTimestamp(1).toLocalDateTime();
                return null;
            }
        }
    }

    public LocalDateTime getScadenzaEditing(int idConferenza) throws SQLException {
        String q = "SELECT scadenza_editing FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getTimestamp(1) != null)
                    return rs.getTimestamp(1).toLocalDateTime();
                return null;
            }
        }
    }

    public LocalDateTime getScadenzaSottomissione3(int idConferenza) throws SQLException {
        String q = "SELECT scadenza_sottomissione_3 FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getTimestamp(1) != null)
                    return rs.getTimestamp(1).toLocalDateTime();
                return null;
            }
        }
    }

    public LocalDateTime getScadenzaImpaginazione(int idConferenza) throws SQLException {
        String q = "SELECT scadenza_impaginazione FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getTimestamp(1) != null)
                    return rs.getTimestamp(1).toLocalDateTime();
                return null;
            }
        }
    }
}