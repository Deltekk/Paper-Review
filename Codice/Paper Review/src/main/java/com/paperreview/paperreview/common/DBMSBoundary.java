package com.paperreview.paperreview.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBMSBoundary {

    private static final String baseUrl = "jdbc:mariadb://paperreview.serverdc.ddnsfree.com:3306/paper_review?useSSL=false&serverTimezone=Europe/Rome";
    private static final String user = "root";
    private static final String pwd = "R5x1!9UQUMLbtng!1tdAA2vY7Vge@%fJH!*5gsbueV6VJg5jNkGfQPQf&qam6q2K";


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
        if (connection == null || connection.isClosed()) {
            init(); // prova a riconnettere
        }
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
