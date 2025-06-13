package paperreviewserver.common.dao;

import paperreviewserver.entities.RevisioneEntity;

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
        String query = "SELECT ref_utente " +
                "FROM Ruolo_conferenza " +
                "WHERE (ruolo = 'Revisore' OR ruolo = 'Sottorevisore') " +
                " AND ref_conferenza = ? " +
                " AND ref_utente NOT IN (" +
                "   SELECT r.ref_utente " +
                "   FROM Revisione r " +
                "   JOIN Paper p ON r.ref_paper = p.id_paper " +
                "   WHERE p.ref_conferenza = ?)";

        List<Integer> revisoriSenzaRevisione = new ArrayList<Integer>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idConferenza);
            ps.setInt(2, idConferenza);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    revisoriSenzaRevisione.add(rs.getInt("ref_utente"));
                }
            }
        }
        return revisoriSenzaRevisione;
    }
}