package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PetDAO {

    public void getAllPets() {

        String sql = "SELECT * FROM Pets";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                System.out.println(
                    result.getInt("pet_id") + " | " +
                    result.getString("name") + " | " +
                    result.getString("type") + " | " +
                    result.getString("breed") + " | " +
                    result.getInt("age") + " | " +
                    result.getString("listing_status") + " | " +
                    result.getString("pet_status")
                );
            }

            result.close();
            statement.close();
            DatabaseConnection.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }   public void getPetById(int petId) {

    String sql = "SELECT * FROM Pets WHERE pet_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, petId);

        ResultSet result = statement.executeQuery();

        if (result.next()) {

            System.out.println(
                result.getInt("pet_id") + " | " +
                result.getString("name") + " | " +
                result.getString("type") + " | " +
                result.getString("breed") + " | " +
                result.getInt("age") + " | " +
                result.getString("listing_status") + " | " +
                result.getString("pet_status")
            );

        } else {
            System.out.println("Pet not found.");
        }

        result.close();
        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void addPet(int shelterUserId, String name, String type,
                   String breed, int age, String description,
                   String photo) {

    String sql = "INSERT INTO Pets " +
                 "(shelter_user_id, name, type, breed, age, " +
                 "description, photo) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, shelterUserId);
        statement.setString(2, name);
        statement.setString(3, type);
        statement.setString(4, breed);
        statement.setInt(5, age);
        statement.setString(6, description);
        statement.setString(7, photo);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Pet added successfully!");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void updatePet(int petId, String name, String type,
                      String breed, int age, String description,
                      String photo) {

    String sql = "UPDATE Pets SET " +
                 "name = ?, type = ?, breed = ?, age = ?, " +
                 "description = ?, photo = ? " +
                 "WHERE pet_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, name);
        statement.setString(2, type);
        statement.setString(3, breed);
        statement.setInt(4, age);
        statement.setString(5, description);
        statement.setString(6, photo);
        statement.setInt(7, petId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Pet updated successfully!");
        } else {
            System.out.println("Pet not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void updatePetListingStatus(int petId, String status) {

    String sql = "UPDATE Pets SET listing_status = ? WHERE pet_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, status);
        statement.setInt(2, petId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Pet listing status updated successfully!");
        } else {
            System.out.println("Pet not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void deletePet(int petId) {

    String sql = "DELETE FROM Pets WHERE pet_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, petId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Pet deleted successfully!");
        } else {
            System.out.println("Pet not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}