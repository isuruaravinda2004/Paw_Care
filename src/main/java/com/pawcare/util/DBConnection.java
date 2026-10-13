package com.pawcare.util;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.Properties;

/** Singleton configuration provider. Each caller owns and closes its own JDBC connection. */
public final class DBConnection {
    private static final DBConnection INSTANCE = new DBConnection();
    private DBConnection() { }
    public static DBConnection getInstance() { return INSTANCE; }

    public Connection getConnection() throws SQLException {
        Properties config = new Properties();
        try (InputStream in = DBConnection.class.getResourceAsStream("/defaults.properties")) {
            if (in != null) config.load(in);
            Path file = Path.of(System.getProperty("pawcare.config", "config.properties"));
            if (Files.exists(file)) {
                try (InputStream local = Files.newInputStream(file)) { config.load(local); }
            }
        } catch (IOException ex) { throw new SQLException("Cannot read config.properties.", ex); }
        // -Dpawcare.db.* is useful for isolated tests. PAWCARE_DB_* can override local config.
        String url = setting("url", config);
        String user = setting("user", config);
        String password = setting("password", config);
        return DriverManager.getConnection(url, user, password);
    }
    private String setting(String key, Properties config) {
        String env = System.getenv("PAWCARE_DB_" + key.toUpperCase());
        return System.getProperty("pawcare.db." + key,
                env != null ? env : config.getProperty("db." + key, ""));
    }
}
