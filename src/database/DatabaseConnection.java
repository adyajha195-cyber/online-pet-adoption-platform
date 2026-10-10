package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Handles the connection between the application and MySQL database.
 */
public class DatabaseConnection {

    // Each of these can be overridden with an environment variable (DB_URL, DB_USER,
    // DB_PASSWORD), so nobody has to edit this file or commit their real password.
    private static final String URL = env("DB_URL", "jdbc:mysql://localhost:3306/pet_adoption");

    private static final String USER = env("DB_USER", "root");

    private static final String PASSWORD = env("DB_PASSWORD", "your password here");

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return (value == null || value.isEmpty()) ? fallback : value;
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
            URL,
            USER,
            PASSWORD
        );
    }

    public static void closeConnection(Connection connection) {

        try {

            if (connection != null) {
                connection.close();
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        try {

            Connection connection = getConnection();

            System.out.println(
                "Database connected successfully!"
            );

            closeConnection(connection);

        } catch (SQLException e) {

            System.out.println(
                "Database connection failed!"
            );

            e.printStackTrace();
        }
    }
}
