package org.ktoskazalnet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/study_db";

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "psql");
        return properties;
    }

    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, getProperties());
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
