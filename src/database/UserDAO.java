package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public void getAllUsers() {

        String sql = "SELECT * FROM Users";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                System.out.println(
                    result.getInt("user_id") + " | " +
                    result.getString("name") + " | " +
                    result.getString("email") + " | " +
                    result.getString("role")
                );
            }

            result.close();
            statement.close();
           DatabaseConnection.closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    public void getUserById(int userId) {

    String sql = "SELECT * FROM Users WHERE user_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, userId);

        ResultSet result = statement.executeQuery();

        if (result.next()) {

            System.out.println(
                result.getInt("user_id") + " | " +
                result.getString("name") + " | " +
                result.getString("email") + " | " +
                result.getString("role")
            );

        } else {
            System.out.println("User not found.");
        }

        result.close();
        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void addUser(String name, String email, String password,
                    String role, String contact, String address,
                    String city, String state) {

    String sql = "INSERT INTO Users " +
                 "(name, email, password, role, contact, address, city, state) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setString(2, email);
        statement.setString(3, password);
        statement.setString(4, role);
        statement.setString(5, contact);
        statement.setString(6, address);
        statement.setString(7, city);
        statement.setString(8, state);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("User added successfully!");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public void updateUser(int userId, String name, String email,
                       String role, String contact, String address,
                       String city, String state) {

    String sql = "UPDATE Users SET " +
                 "name = ?, email = ?, role = ?, contact = ?, " +
                 "address = ?, city = ?, state = ? " +
                 "WHERE user_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setString(2, email);
        statement.setString(3, role);
        statement.setString(4, contact);
        statement.setString(5, address);
        statement.setString(6, city);
        statement.setString(7, state);
        statement.setInt(8, userId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("User updated successfully!");
        } else {
            System.out.println("User not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
public void deleteUser(int userId) {

    String sql = "DELETE FROM Users WHERE user_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, userId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("User not found.");
        }

        statement.close();
       DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}