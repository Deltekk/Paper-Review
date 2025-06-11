package paperreviewserver.common.dao;

import java.sql.*;
import java.time.LocalDateTime;

public class ConferenzaDao {
    private final Connection connection;

    public ConferenzaDao(Connection connection) {
        this.connection = connection;
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