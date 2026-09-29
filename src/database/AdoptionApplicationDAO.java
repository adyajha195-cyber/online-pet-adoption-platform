package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Handles database operations for adoption applications.
 */
public class AdoptionApplicationDAO {

    public void getAllApplications() {

        String sql = "SELECT * FROM AdoptionApplications";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                System.out.println(
                    result.getInt("application_id") + " | " +
                    result.getInt("adopter_id") + " | " +
                    result.getInt("pet_id") + " | " +
                    result.getString("application_details") + " | " +
                    result.getString("application_date") + " | " +
                    result.getString("status")
                );
            }

            result.close();
            statement.close();
            DatabaseConnection.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   public void getApplicationById(int applicationId) {

    String sql = "SELECT * FROM AdoptionApplications WHERE application_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, applicationId);

        ResultSet result = statement.executeQuery();

        if (result.next()) {

            System.out.println(
                result.getInt("application_id") + " | " +
                result.getInt("adopter_id") + " | " +
                result.getInt("pet_id") + " | " +
                result.getString("application_details") + " | " +
                result.getString("application_date") + " | " +
                result.getString("status")
            );

        } else {
            System.out.println("Application not found.");
        }

        result.close();
        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void addApplication(int adopterId, int petId, String applicationDetails) {

    String sql = "INSERT INTO AdoptionApplications " +
                 "(adopter_id, pet_id, application_details) " +
                 "VALUES (?, ?, ?)";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, adopterId);
        statement.setInt(2, petId);
        statement.setString(3, applicationDetails);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Application added successfully!");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void updateApplicationStatus(int applicationId, String status) {

    String sql = "UPDATE AdoptionApplications " +
                 "SET status = ? " +
                 "WHERE application_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, status);
        statement.setInt(2, applicationId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Application status updated successfully!");
        } else {
            System.out.println("Application not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void deleteApplication(int applicationId) {

    String sql = "DELETE FROM AdoptionApplications WHERE application_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, applicationId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Application deleted successfully!");
        } else {
            System.out.println("Application not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}