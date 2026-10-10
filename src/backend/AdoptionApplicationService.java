package backend;

import java.sql.SQLException;
import java.util.List;
import model.AdoptionApplication;

public interface AdoptionApplicationService {

    // Submit a new adoption application with no details text
    // (returns false if the pet is not available or already applied for)
    boolean submitApplication(int adopterId, int petId)
            throws SQLException;

    // Submit a new adoption application with the adopter's reason
    boolean submitApplication(
            int adopterId,
            int petId,
            String details
    ) throws SQLException;

    // Retrieve all adoption applications
    List<AdoptionApplication> getAllApplications()
            throws SQLException;

    // Retrieve an application by its ID
    AdoptionApplication getApplicationById(int applicationId)
            throws SQLException;

    // Retrieve applications submitted by an adopter
    List<AdoptionApplication> getApplicationsByAdopter(int adopterId)
            throws SQLException;

    // Retrieve applications associated with a shelter
    List<AdoptionApplication> getApplicationsByShelter(int shelterUserId)
            throws SQLException;

    // Retrieve the status of an application
    String getApplicationStatus(int applicationId)
            throws SQLException;

    // Update an application's status
    boolean updateApplicationStatus(int applicationId, String status)
            throws SQLException;
}
