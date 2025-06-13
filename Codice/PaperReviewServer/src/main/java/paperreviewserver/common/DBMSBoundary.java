package paperreviewserver.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBMSBoundary {

    private static final String baseUrl = DotenvUtil.getDBBaseUrl();
    private static final String user = DotenvUtil.getDbUser();
    private static final String pwd = DotenvUtil.getDbPassword();


    private static Connection connection;

    public static void init() {
        try {
            connection = DriverManager.getConnection(baseUrl, user, pwd);
            System.out.println("[DB] Connessione al DB riuscita");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DB] Errore nella connessione al DB");
        }
    }

    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static Connection getConnection() throws SQLException {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[DB] Connessione chiusa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
