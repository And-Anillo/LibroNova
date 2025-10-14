package com.libronova.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Coder
 */
public class ConexionDB {

    private static Connection connection;

    private ConexionDB() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = ConfigLoader.get("db.url");
            String user = ConfigLoader.get("db.user");
            String password = ConfigLoader.get("db.password");

            connection = DriverManager.getConnection(url, user, password);
            System.out.println(" Conexión exitosa a la base de datos");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }

}
