package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/pet_adoption";

    private static final String USER = "root";

    private static final String PASSWORD = "Adya123";

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