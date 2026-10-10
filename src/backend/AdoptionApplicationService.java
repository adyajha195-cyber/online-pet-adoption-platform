package backend;

import model.AdoptionApplication;
import java.sql.SQLException;
import java.util.List;

public interface AdoptionApplicationService {

    boolean submitApplication(int adopterId, int petId)
            throws SQLException;

    boolean submitApplication(
            int adopterId,
            int petId,
            String details
    ) throws SQLException;

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