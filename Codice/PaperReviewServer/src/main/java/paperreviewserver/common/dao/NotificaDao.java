package paperreviewserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificaDao {
    private final Connection connection;

    public NotificaDao(Connection connection) {
        this.connection = connection;
    }

    public void inserisciNotifica(int refUtente, int refConferenza, String testo) throws SQLException {
        String query = "INSERT INTO Notifica (data, testo, isLetta, ref_utente, ref_conferenza) VALUES (NOW(), ?, false, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, testo);
            stmt.setInt(2, refUtente);
            stmt.setInt(3, refConferenza);
            stmt.executeUpdate();
        }
    }
}