package com.libronova.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Utility class for managing database connections and retrieving application configuration.
 * Loads settings from 'config.properties' at class initialization time.
 * Provides centralized access to database credentials and business rules (e.g., loan duration, fine rate).
 */
public class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    private static Properties props;

    /**
     * Static initializer: loads configuration from 'config.properties' file located in the classpath.
     * Logs an error if the file is missing or unreadable.
     */
    static {
        props = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
        } catch (IOException e) {
            logger.severe("Error al cargar config.properties: " + e.getMessage());
        }
    }

    /**
     * Establishes and returns a new database connection using credentials from config.properties.
     * @return a valid JDBC Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Returns the maximum number of days allowed for a book loan.
     * Defaults to 7 days if the property is not defined in config.properties.
     */
    public static int getDiasPrestamo() {
        return Integer.parseInt(props.getProperty("diasPrestamo", "7"));
    }

    /**
     * Returns the daily fine amount (in local currency) for overdue books.
     * Defaults to 1500 if the property is not defined in config.properties.
     */
    public static double getMultaPorDia() {
        return Double.parseDouble(props.getProperty("multaPorDia", "1500"));
    }
}