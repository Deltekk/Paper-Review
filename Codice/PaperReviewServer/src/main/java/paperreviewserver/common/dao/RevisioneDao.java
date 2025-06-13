package paperreviewserver.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RevisioneDao {
    private final Connection connection;

    public RevisioneDao(Connection connection) {
        this.connection = connection;
    }

    public List<Integer> getRevisoriSenzaRevisione(int idConferenza) throws SQLException {
        String query = "SELECT r.ref_utente " +
                "FROM Revisione r " +
                "JOIN Paper p ON r.ref_paper = p.id_paper " +  // Join tra Revisione e Paper\n" +
                "WHERE p.ref_conferenza = ? AND (r.testo IS NULL " +  // Filtra per la conferenza AND (r.testo IS NULL " +
                "OR r.valutazione IS NULL " +
                "OR r.data_sottomissione IS NULL " +
                "OR r.punti_forza IS NULL OR " +
                "r.punti_debolezza IS NULL)";
        List<Integer> revisoriSenzaRevisione = new ArrayList<Integer>();
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