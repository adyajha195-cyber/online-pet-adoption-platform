package database;

import java.sql.SQLException;

public class AdoptionApplicationDAOTest {

    public static void main(String[] args) {
        AdoptionApplicationDAO applicationDAO = new AdoptionApplicationDAO();

        try {
             boolean success = applicationDAO.updateApplicationStatus(2, "Approved");

            if (success) {
                System.out.println("Application status updated successfully!");
            } else {
                System.out.println("Update failed: Application ID not found.");
            }

        } catch (SQLException e) {
            System.err.println("Database error occurred while updating status:");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status parameter passed:");
            e.printStackTrace();
        }
    }
}
