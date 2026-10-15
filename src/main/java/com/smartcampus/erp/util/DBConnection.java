package com.smartcampus.erp.util;

import com.smartcampus.erp.exception.DatabaseOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Rubric: Database Connectivity (JDBC) - Utility Class
public final class DBConnection {

    private static String url;
    private static String username;
    private static String password;
    private static String driverClassName;

    static {
        loadConfiguration();
    }

    private DBConnection() {
    }

    private static void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to load application.properties for JDBC: " + e.getMessage());
        }

        // Read environment variables first, then application.properties, then fallback defaults
        url = System.getenv("DB_URL");
        if (url == null || url.isBlank()) {
            url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/smartcampus_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
            // Expand placeholder if present: ${DB_URL:default}
            if (url.startsWith("${") && url.contains(":")) {
                int colonIdx = url.indexOf(':');
                url = url.substring(colonIdx + 1, url.length() - 1);
            }
        }

        username = System.getenv("DB_USER");
        if (username == null || username.isBlank()) {
            username = props.getProperty("db.username", "root");
            if (username.startsWith("${") && username.contains(":")) {
                int colonIdx = username.indexOf(':');
                username = username.substring(colonIdx + 1, username.length() - 1);
            }
        }

        password = System.getenv("DB_PASSWORD");
        if (password == null) {
            password = props.getProperty("db.password", "password");
            if (password != null && password.startsWith("${") && password.contains(":")) {
                int colonIdx = password.indexOf(':');
                password = password.substring(colonIdx + 1, password.length() - 1);
            }
        }

        driverClassName = props.getProperty("db.driver-class-name", "com.mysql.cj.jdbc.Driver");

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            throw new DatabaseOperationException("JDBC Driver not found: " + driverClassName, e);
        }
    }

    // Rubric: JDBC Connection method
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }
}
