package paperreviewserver.common.dbms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProceedingDao {
    private final Connection connection;

    public ProceedingDao(Connection connection) {
        this.connection = connection;
    }

    public List<Object[]> getProceedingsNonSottomessiByConferenza(int idConferenza) throws SQLException {
        List<Object[]> proceedingsNonSottomessi = new ArrayList<>();

        // Query per ottenere i proceedings non sottomessi
        String query = "SELECT p.id_proceeding, p.titolo, p.data_sottomissione, p.ref_utente, p.ref_conferenza " +
                "FROM Proceeding p " +
                "WHERE p.ref_conferenza = ? " +  // Filtra per conferenza
                "AND (p.titolo IS NULL " +      // Controlla se il titolo è NULL
                "OR p.data_sottomissione IS NULL " +  // Controlla se la data di sottomissione è NULL
                "OR p.ref_utente IS NULL " +    // Controlla se il riferimento all'autore è NULL
                "OR p.ref_conferenza IS NULL)"; // Controlla se il riferimento alla conferenza è NULL

        // Esegui la query
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);  // Imposta il parametro per l'ID della conferenza

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Aggiungi i risultati nella lista
                    proceedingsNonSottomessi.add(new Object[]{
                            rs.getInt("id_proceeding"),
                            rs.getString("titolo"),
                            rs.getTimestamp("data_sottomissione"),
                            rs.getInt("ref_utente"),
                            rs.getInt("ref_conferenza")
                    });
                }
            }
        }

        return proceedingsNonSottomessi;
    }

    public List<Integer> getEditorByConferenza(int idConferenza) throws SQLException {
        List<Integer> editori = new ArrayList<>();

        String query = "SELECT ref_utente FROM Ruolo_conferenza " +
                "WHERE ruolo = 'Editor' AND ref_conferenza = ?";  // Filtro per 'Editor' e la conferenza specifica

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idConferenza);  // Imposta il parametro per la conferenza
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    editori.add(rs.getInt("ref_utente"));  // Aggiunge l'ID dell'editor alla lista
                }
            }
        }

        return editori;  // Restituisce la lista degli ID degli editori
    }
}
