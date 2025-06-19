package paperreviewserver.common.dbms.dao;

import paperreviewserver.entities.PaperEntity;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaperDao {
    private final Connection connection;

    public PaperDao(Connection connection) {
        this.connection = connection;
    }

    public List<Integer> getAutoriSenzaPaper(int idConferenza) throws SQLException {
        String query = "SELECT ref_utente " +
                "FROM Ruolo_conferenza " +
                "WHERE ruolo = 'Autore' " +
                "AND ref_conferenza = ? " +
                "AND ref_utente NOT IN(" +
                "   SELECT p.ref_utente " +
                "   FROM Paper p" +
                "   WHERE p.ref_conferenza = ?)";

        List<Integer> autoriSenzaPaper = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            stmt.setInt(2, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    autoriSenzaPaper.add(rs.getInt("ref_utente"));
                }
            }
        }
        return autoriSenzaPaper;
    }

    public List<Object[]> getPapersSenzaFileByConferenza(int idConferenza) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String query = "SELECT id_paper, titolo, ref_utente FROM Paper WHERE ref_conferenza = ? AND file IS NULL";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                            rs.getInt("id_paper"),
                            rs.getString("titolo"),
                            rs.getInt("ref_utente")
                    });
                }
            }
        }
        return rows;
    }

    public List<Object[]> getPapersByDataSottomissione(int idConferenza, LocalDateTime dataLimite) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String query = "SELECT p.ref_utente, p.titolo " +
                "FROM Paper p " +
                "WHERE p.ref_conferenza = ? AND p.data_sottomissione < ?";  // Filtra per data sottomissione

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);  // Imposta il parametro per la conferenza
            stmt.setTimestamp(2, Timestamp.valueOf(dataLimite));  // Imposta il parametro per la data limite

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                            rs.getInt("ref_utente"),
                            rs.getString("titolo")
                    });
                }
            }
        }
        return rows;
    }

    public List<Object[]> getPapersByDataSottomissioneMediaVoto(int idConferenza, LocalDateTime dataLimite) throws SQLException {
        List<Object[]> rows = new ArrayList<>();

        // La query ora include il calcolo della media delle valutazioni e ordina per la media in ordine decrescente
        String query = "SELECT p.ref_utente, p.titolo, AVG(r.valutazione) AS media_voto " +
                "FROM Paper p " +
                "LEFT JOIN Revisione r ON p.id_paper = r.ref_paper " +
                "WHERE p.ref_conferenza = ? AND p.data_sottomissione < ? " +
                "GROUP BY p.id_paper " +
                "ORDER BY media_voto DESC";  // Ordina per la media dei voti in ordine decrescente

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);  // Imposta il parametro per la conferenza
            stmt.setTimestamp(2, Timestamp.valueOf(dataLimite));  // Imposta il parametro per la data limite

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                            rs.getInt("ref_utente"),  // ID dell'utente (autore)
                            rs.getString("titolo"),    // Titolo del paper
                            rs.getDouble("media_voto") // Media della valutazione
                    });
                }
            }
        }
        return rows;
    }

    public List<PaperEntity> getByConferenza(int idConferenza) throws SQLException {
        List<PaperEntity> papers = new ArrayList<>();
        String query = "SELECT id_paper, titolo, ref_utente, data_sottomissione FROM Paper WHERE ref_conferenza = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PaperEntity paper = new PaperEntity();
                    paper.setId(rs.getInt("id_paper"));
                    paper.setTitolo(rs.getString("titolo"));
                    paper.setRefUtente(rs.getInt("ref_utente"));
                    paper.setDataSottomissione(rs.getTimestamp("data_sottomissione") != null
                            ? rs.getTimestamp("data_sottomissione").toLocalDateTime() : null);
                    papers.add(paper);
                }
            }
        }
        return papers;
    }
}