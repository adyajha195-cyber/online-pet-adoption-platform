package model;

/** Plain data object for one row of the AdoptionApplication table. Immutable. */
public class AdoptionApplication {

    private final int applicationId;
    private final int adopterId;
    private final int petId;
    private final String applicationDetails;
    private final String applicationDate;
    private final String status;

    public AdoptionApplication(int applicationId, int adopterId, int petId, String applicationDetails, String applicationDate, String status) {
        this.applicationId = applicationId;
        this.adopterId = adopterId;
        this.petId = petId;
        this.applicationDetails = applicationDetails;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    public int getApplicationId() { return applicationId; }
    public int getAdopterId() { return adopterId; }
    public int getPetId() { return petId; }
    public String getApplicationDetails() { return applicationDetails; }
    public String getApplicationDate() { return applicationDate; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return applicationId + " | " + adopterId + " | " + petId + " | " + applicationDetails + " | " + applicationDate + " | " + status;
    }
}
