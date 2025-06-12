package paperreviewserver.common.dao;

import java.sql.*;
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
                "WHERE ref_conferenza = ? " +
                "AND ref_utente NOT IN (" +
                "   SELECT ref_utente " +
                "   FROM Paper " +
                "   WHERE ref_conferenza = ?" +
                ")";

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
}