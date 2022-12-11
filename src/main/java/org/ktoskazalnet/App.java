package org.ktoskazalnet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/study_db?user=postgres&password=psql";

    public static void main( String[] args ) {

    }

    private static void initDb() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS public.course (" +
                    "uuid TEXT NOT NULL, " +
                    "name TEXT NOT NULL," +
                    "PRIMARY KEY (name)" +
                    ")";

            String sql2 = "CREATE TABLE IF NOT EXISTS public.users (" +
                    "id SERIAL," +
                    "name TEXT NOT NULL," +
                    "age INTEGER NOT NULL," +
                    "course TEXT," +
                    "PRIMARY KEY (id)," +
                    "FOREIGN KEY (course)" +
                    "REFERENCES public.course (name)" +
                    ");";

            statement.execute(sql);
            statement.execute(sql2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
