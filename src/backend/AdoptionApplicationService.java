package backend;

public interface AdoptionApplicationService {

    /** Same as submitApplication(adopterId, petId, "") - kept so existing calls still compile. */
    boolean submitApplication(int adopterId, int petId);

    /**
     * Submits an application.
     * @return true if submitted; false if not allowed (pet not approved/available, or
     *         this adopter already applied for this pet)
     * @throws ServiceException if the database fails (e.g. adopterId doesn't exist, DB down)
     */
    boolean submitApplication(int adopterId, int petId, String applicationDetails);

    /**
     * @param status Pending, Approved or Rejected
     * @return true if updated; false if the application doesn't exist or status is invalid
     * @throws ServiceException if the database fails
     */
    boolean updateApplicationStatus(int applicationId, String status);

    /** @return the status, or null if the application doesn't exist */
    String getApplicationStatus(int applicationId);
}
