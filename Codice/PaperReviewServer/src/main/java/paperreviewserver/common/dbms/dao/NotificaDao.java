package paperreviewserver.common.dbms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class NotificaDao {
    private final Connection connection;

    public NotificaDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Inserisce una nuova notifica.
     *
     * @param refUtente       id dell’utente destinatario
     * @param refConferenza   id della conferenza
     * @param testo           testo della notifica
     */
    public void inserisciNotifica(int refUtente, int refConferenza, String testo) throws SQLException {
        String query = "INSERT INTO Notifica (data, testo, isLetta, ref_utente, ref_conferenza) VALUES (?, ?, false, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, testo);
            stmt.setInt(3, refUtente);
            stmt.setInt(4, refConferenza);
            stmt.executeUpdate();
        }
    }
}