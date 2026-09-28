package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO {

    public void login(String email, String password) {

        String sql = "SELECT * FROM Users WHERE email = ? AND password = ?";

        try {

            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {

                System.out.println("Login successful!");
                System.out.println("User ID: " + result.getInt("user_id"));
                System.out.println("Name: " + result.getString("name"));
                System.out.println("Role: " + result.getString("role"));

            } else {

                System.out.println("Invalid email or password.");

            }

            result.close();
            statement.close();

            DatabaseConnection.closeConnection(connection);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}