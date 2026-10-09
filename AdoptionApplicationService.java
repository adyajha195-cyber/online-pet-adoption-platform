public interface AdoptionApplicationService {

    boolean submitApplication(int adopterId, int petId);

    boolean updateApplicationStatus(int applicationId, String status);

    String getApplicationStatus(int applicationId);
}
