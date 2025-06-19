package paperreviewserver.common.dbms.dao;

import paperreviewserver.entities.ConferenzaEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConferenzaDao {
    private final Connection connection;

    public ConferenzaDao(Connection connection) {
        this.connection = connection;
    }

    public List<ConferenzaEntity> getAll() throws SQLException {
        List<ConferenzaEntity> lista = new ArrayList<>();

        String query = """
            SELECT id_conferenza, nome, giorni_preavviso, metodo_assegnazione,
                   scadenza_sottomissione, scadenza_revisione, scadenza_sottomissione_2,
                   scadenza_editing, scadenza_sottomissione_3, scadenza_impaginazione
            FROM Conferenza
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ConferenzaEntity c = new ConferenzaEntity();
                c.setId(rs.getInt("id_conferenza"));
                c.setNome(rs.getString("nome"));
                c.setGiorniPreavviso(rs.getInt("giorni_preavviso"));
                c.setModalitaDistribuzione(rs.getString("metodo_assegnazione"));
                c.setScadenzaSottomissione(getLocalDateTime(rs, "scadenza_sottomissione"));
                c.setScadenzaRevisione(getLocalDateTime(rs, "scadenza_revisione"));
                c.setScadenzaSottomissione2(getLocalDateTime(rs, "scadenza_sottomissione_2"));
                c.setScadenzaEditing(getLocalDateTime(rs, "scadenza_editing"));
                c.setScadenzaSottomissione3(getLocalDateTime(rs, "scadenza_sottomissione_3"));
                c.setScadenzaImpaginazione(getLocalDateTime(rs, "scadenza_impaginazione"));

                lista.add(c);
            }
        }

        return lista;
    }

    private LocalDateTime getLocalDateTime(ResultSet rs, String column) throws SQLException {
        Timestamp ts = rs.getTimestamp(column);
        return (ts != null) ? ts.toLocalDateTime() : null;
    }
    public Map<Integer, String> getAllIdsAndNomi() throws SQLException {
        Map<Integer, String> map = new HashMap<>();
        String q = "SELECT id_conferenza, nome FROM Conferenza";
        try (PreparedStatement stmt = connection.prepareStatement(q);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                map.put(rs.getInt("id_conferenza"), rs.getString("nome"));
            }
        }
        return map;
    }

    public Integer getRateAccettazione(int idConferenza) throws SQLException {
        String q = "SELECT rate_accettazione FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("rate_accettazione");
            }
        }
        return null;
    }

    public LocalDateTime getScadenzaSottomissione(int idConferenza) throws SQLException {
        String query = "SELECT scadenza_sottomissione FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("scadenza_sottomissione");
                    return ts != null ? ts.toLocalDateTime() : null;
                }
            }
        }
        return null;
    }

    public LocalDateTime getScadenzaSottomissione2(int idConferenza) throws SQLException {
        String q = "SELECT scadenza_sottomissione_2 FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getTimestamp(1).toLocalDateTime();
            }
        }
        return null;
    }

    public LocalDateTime getScadenzaSottomissione3(int idConferenza) throws SQLException {
        String q = "SELECT scadenza_sottomissione_3 FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getTimestamp(1).toLocalDateTime();
            }
        }
        return null;
    }

    public LocalDateTime getScadenzaRevisione(int idConferenza) throws SQLException {
        String query = "SELECT scadenza_revisione FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("scadenza_revisione");
                    return ts != null ? ts.toLocalDateTime() : null;
                }
            }
        }
        return null;
    }

    public LocalDateTime getScadenzaEditing(int idConferenza) throws SQLException {
        String query = "SELECT scadenza_editing FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("scadenza_editing");
                    return ts != null ? ts.toLocalDateTime() : null;
                }
            }
        }
        return null;
    }

    public LocalDateTime getScadenzaImpaginazione(int idConferenza) throws SQLException {
        String query = "SELECT scadenza_impaginazione FROM Conferenza WHERE id_conferenza = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("scadenza_impaginazione");
                    return ts != null ? ts.toLocalDateTime() : null;
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
                if (rs.next()) return rs.getInt("giorni_preavviso");
            }
        }
        return null;
    }
}
