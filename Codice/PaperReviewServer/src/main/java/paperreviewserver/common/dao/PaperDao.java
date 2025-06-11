package paperreviewserver.common.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaperDao {
    private final Connection connection;

    public PaperDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Restituisce la lista degli id degli autori che NON hanno ancora sottomesso il paper per una conferenza
     */
    public List<Object[]> getPapersIdTitoloUtenteByConferenza(int idConferenza) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String query = "SELECT id_paper, titolo, ref_utente FROM Paper WHERE ref_conferenza = ?";
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