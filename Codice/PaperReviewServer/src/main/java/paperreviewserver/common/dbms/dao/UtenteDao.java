package paperreviewserver.common.dbms.dao;

import paperreviewserver.entities.UtenteEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDao {
    private final Connection connection;

    public UtenteDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Restituisce una mappa id_utente → [nome, cognome, email]
     */
    public List<Object[]> getUtentiInfoByIds(List<Integer> ids) throws SQLException {
        List<Object[]> results = new ArrayList<>();
        if (ids == null || ids.isEmpty())
            return results;

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            placeholders.append("?");
            if (i < ids.size() - 1)
                placeholders.append(",");
        }

        String q = "SELECT id_utente, nome, cognome, email FROM Utente WHERE id_utente IN (" + placeholders + ")";
        try (PreparedStatement stmt = connection.prepareStatement(q)) {
            for (int i = 0; i < ids.size(); i++) {
                stmt.setInt(i + 1, ids.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Object[] {
                            rs.getInt("id_utente"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email")
                    });
                }
            }
        }
        return results;
    }

    public Object[] getUtenteById(int idUtente) throws SQLException {
        String query = "SELECT nome, cognome, email FROM Utente WHERE id_utente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idUtente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[] {
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("email")
                    };
                }
            }
        }
        return null;  // Se l'utente non viene trovato
    }

    public List<UtenteEntity> getRevisoriConferenza(int idConferenza) throws SQLException {
        List<UtenteEntity> revisori = new ArrayList<>();

        String query = """
        SELECT u.id_utente, u.nome, u.cognome, u.email
        FROM Utente u
        JOIN Ruolo_conferenza rc ON u.id_utente = rc.ref_utente
        WHERE rc.ref_conferenza = ? AND rc.ruolo = 'Revisore'
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UtenteEntity u = new UtenteEntity();
                    u.setId(rs.getInt("id_utente"));
                    u.setNome(rs.getString("nome"));
                    u.setCognome(rs.getString("cognome"));
                    u.setEmail(rs.getString("email"));
                    revisori.add(u);
                }
            }
        }

        return revisori;
    }
}