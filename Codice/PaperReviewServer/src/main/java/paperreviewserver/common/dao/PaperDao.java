package paperreviewserver.dao;

import paperreviewserver.entities.UtenteEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaperDao {
    private final Connection connection;

    public PaperDao(Connection connection) {
        this.connection = connection;
    }

    // Trova autori di una conferenza che NON hanno sottomesso paper
    public List<UtenteEntity> getAutoriNonSottomissori(int idConferenza) throws SQLException {
        List<UtenteEntity> results = new ArrayList<>();
        String query =
                "SELECT u.id_utente, u.nome, u.cognome, u.email " +
                        "FROM Utente u " +
                        "JOIN Ruolo_conferenza rc ON rc.ref_utente = u.id_utente " +
                        "WHERE rc.ruolo = 'Autore' AND rc.ref_conferenza = ? " +
                        "AND u.id_utente NOT IN (" +
                        "   SELECT p.ref_utente FROM Paper p WHERE p.ref_conferenza = ?" +
                        ")";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            stmt.setInt(2, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UtenteEntity u = new UtenteEntity();
                    u.setId(rs.getInt("id_utente"));
                    u.setNome(rs.getString("nome"));
                    u.setCognome(rs.getString("cognome"));
                    u.setEmail(rs.getString("email"));
                    results.add(u);
                }
            }
        }
        return results;
    }
}