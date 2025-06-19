package paperreviewserver.common.dbms;

import paperreviewserver.common.DotenvUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBMSBoundary {

    private static final String baseUrl = DotenvUtil.getDBBaseUrl();
    private static final String user = DotenvUtil.getDbUser();
    private static final String pwd = DotenvUtil.getDbPassword();

    /**
     * Restituisce una nuova connessione al database.
     * Ogni connessione sarà separata per ogni operazione.
     *
     * @return una nuova connessione al database
     * @throws SQLException se la connessione non può essere stabilita
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(baseUrl, user, pwd);
            System.out.println("[DB] Connessione al DB riuscita");
            return connection;
        } catch (SQLException e) {
            System.err.println("[DB] Errore nella connessione al DB");
            throw e;
        }
    }

    /**
     * Chiude la connessione al database, se è ancora aperta.
     *
     * @param connection la connessione da chiudere
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DB] Connessione chiusa correttamente.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}