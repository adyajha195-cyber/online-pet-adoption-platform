package backend;

import java.sql.SQLException;
import java.util.List;
import model.AdoptionApplication;

public interface AdoptionApplicationService {

    /**
     * @return true if the application was created; false if the pet does
     *         not exist, is not an approved/available listing, or the
     *         adopter has already applied for it.
     */
    boolean submitApplication(int adopterId, int petId)
            throws SQLException;

    boolean submitApplication(int adopterId, int petId, String details)
            throws SQLException;

    AdoptionApplication getApplicationById(int applicationId)
            throws SQLException;

    List<AdoptionApplication> getApplicationsByAdopter(int adopterId)
            throws SQLException;

    List<AdoptionApplication> getApplicationsByShelter(int shelterId)
            throws SQLException;

    String getApplicationStatus(int applicationId)
            throws SQLException;

    boolean updateApplicationStatus(int applicationId, String status)
            throws SQLException;
}
