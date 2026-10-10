package backend;

import database.AdoptionApplicationDAO;
import database.PetDAO;
import database.UserDAO;
import java.sql.SQLException;
import java.util.List;
import model.AdoptionApplication;
import model.Pet;
import model.Status;
import model.User;

public class AdoptionApplicationServiceImpl
        implements AdoptionApplicationService {

    private final AdoptionApplicationDAO applicationDAO;
    private final PetDAO petDAO;
    private final UserDAO userDAO;

    public AdoptionApplicationServiceImpl() {
        this.applicationDAO = new AdoptionApplicationDAO();
        this.petDAO = new PetDAO();
        this.userDAO = new UserDAO();
    }

    @Override
    public boolean submitApplication(int adopterId, int petId)
            throws SQLException {

        return submitApplication(adopterId, petId, "");
    }

    @Override
    public boolean submitApplication(
            int adopterId,
            int petId,
            String details
    ) throws SQLException {

        if (adopterId <= 0) {
            throw new IllegalArgumentException("Invalid adopter ID.");
        }

        if (petId <= 0) {
            throw new IllegalArgumentException("Invalid pet ID.");
        }

        if (details == null) {
            throw new IllegalArgumentException(
                    "Application details cannot be null."
            );
        }

        // The applicant must exist and be an Adopter
        User adopter = userDAO.getUserById(adopterId);

        if (adopter == null) {
            throw new ServiceException("Adopter does not exist.");
        }

        if (!Status.ADOPTER.equals(adopter.getRole())) {
            throw new ServiceException(
                    "Only adopters can submit adoption applications."
            );
        }

        // Quick pre-checks for a clean "false" result. The DAO repeats
        // these rules (plus the duplicate check) atomically in SQL, so
        // the DAO is the final authority.
        Pet pet = petDAO.getPetById(petId);

        if (pet == null) {
            return false;
        }

        if (!Status.APPROVED.equalsIgnoreCase(pet.getListingStatus())) {
            return false;
        }

        if (!Status.AVAILABLE.equalsIgnoreCase(pet.getPetStatus())) {
            return false;
        }

        // Returns -1 if blocked (e.g. this adopter already applied)
        int applicationId = applicationDAO.addApplication(
                adopterId,
                petId,
                details.trim()
        );

        return applicationId > 0;
    }

    @Override
    public List<AdoptionApplication> getAllApplications()
            throws SQLException {

        return applicationDAO.getAllApplications();
    }

    @Override
    public AdoptionApplication getApplicationById(int applicationId)
            throws SQLException {

        if (applicationId <= 0) {
            return null;
        }

        return applicationDAO.getApplicationById(applicationId);
    }

    @Override
    public List<AdoptionApplication> getApplicationsByAdopter(
            int adopterId
    ) throws SQLException {

        if (adopterId <= 0) {
            throw new IllegalArgumentException("Invalid adopter ID.");
        }

        return applicationDAO.getApplicationsByAdopter(adopterId);
    }

    @Override
    public List<AdoptionApplication> getApplicationsByShelter(
            int shelterId
    ) throws SQLException {

        if (shelterId <= 0) {
            throw new IllegalArgumentException("Invalid shelter ID.");
        }

        return applicationDAO.getApplicationsByShelter(shelterId);
    }

    @Override
    public String getApplicationStatus(int applicationId)
            throws SQLException {

        AdoptionApplication application =
                getApplicationById(applicationId);

        if (application == null) {
            return null;
        }

        return application.getStatus();
    }

    @Override
    public boolean updateApplicationStatus(
            int applicationId,
            String status
    ) throws SQLException {

        if (applicationId <= 0 || status == null) {
            return false;
        }

        // Invalid status values are rejected with false, not an exception
        try {
            Status.require(Status.APPLICATION, status, "application status");
        } catch (IllegalArgumentException e) {
            return false;
        }

        return applicationDAO.updateApplicationStatus(
                applicationId,
                status
        );
    }
}
