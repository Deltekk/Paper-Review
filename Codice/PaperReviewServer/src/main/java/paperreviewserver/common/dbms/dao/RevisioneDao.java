package paperreviewserver.common.dbms.dao;

import paperreviewserver.entities.RevisioneEntity;

import java.sql.*;
import java.util.*;

public class RevisioneDao {
    private final Connection connection;

    public RevisioneDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Conta quanti paper sono stati assegnati a un revisore in una conferenza.
     */
    public int countPaperAssegnati(int idRevisore, int idConferenza) throws SQLException {
        String query = """
            SELECT COUNT(*) AS count
            FROM Revisione r
            JOIN Paper p ON r.ref_paper = p.id_paper
            WHERE r.ref_utente = ? AND p.ref_conferenza = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idRevisore);
            stmt.setInt(2, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("count");
            }
        }
        return 0;
    }

    /**
     * Restituisce l'insieme degli ID dei revisori già assegnati a un paper.
     */
    public Set<Integer> getRevisoriByPaper(int idPaper) throws SQLException {
        Set<Integer> revisori = new HashSet<>();
        String query = "SELECT ref_utente FROM Revisione WHERE ref_paper = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaper);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    revisori.add(rs.getInt("ref_utente"));
                }
            }
        }
        return revisori;
    }

    /**
     * Assegna un revisore a un paper, creando una riga nella tabella Revisione.
     */
    public void assegnaPaper(int idPaper, int idRevisore) throws SQLException {
        String query = """
            INSERT INTO Revisione (ref_paper, ref_utente)
            VALUES (?, ?)
        """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPaper);
            stmt.setInt(2, idRevisore);
            stmt.executeUpdate();
        }
    }

    /**
     * (Già presente) Lista dei revisori che non hanno completato le revisioni.
     */
    public List<Integer> getRevisoriSenzaRevisione(int idConferenza) throws SQLException {
        String query = "SELECT r.ref_utente " +
                "FROM Revisione r " +
                "JOIN Paper p ON r.ref_paper = p.id_paper " +
                "WHERE p.ref_conferenza = ? AND (r.testo IS NULL " +
                "OR r.valutazione IS NULL " +
                "OR r.data_sottomissione IS NULL " +
                "OR r.punti_forza IS NULL OR " +
                "r.punti_debolezza IS NULL)";
        List<Integer> revisoriSenzaRevisione = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idConferenza);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    revisoriSenzaRevisione.add(rs.getInt("ref_utente"));
                }
            }
        }
        return revisoriSenzaRevisione;
    }
}