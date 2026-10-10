package backend;

import database.AdoptionApplicationDAO;
import database.PetDAO;
import database.UserDAO;

import model.AdoptionApplication;
import model.Pet;
import model.User;
import model.Status;

import java.sql.SQLException;
import java.util.List;

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

        // Validate adopter ID
        if (adopterId <= 0) {
            throw new ServiceException("Invalid adopter ID.");
        }

        // Check whether the adopter exists
        User adopter = userDAO.getUserById(adopterId);

        if (adopter == null) {
            throw new ServiceException(
                    "Invalid adopter: user does not exist."
            );
        }

        // Validate pet ID
        if (petId <= 0) {
            throw new IllegalArgumentException("Invalid pet ID.");
        }

        if (details == null) {
            throw new IllegalArgumentException(
                    "Application details cannot be null."
            );
        }

        // Check whether the pet exists
        Pet pet = petDAO.getPetById(petId);

        if (pet == null) {
            return false;
        }

        // Only approved listings can receive applications
        if (!"Approved".equalsIgnoreCase(pet.getListingStatus())) {
            return false;
        }

        // Only available pets can receive applications
        if (!"Available".equalsIgnoreCase(pet.getPetStatus())) {
            return false;
        }

        // Submit through the existing DAO
        int applicationId = applicationDAO.addApplication(
                adopterId,
                petId,
                details
        );

        return applicationId > 0;
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

        try {
            Status.require(
                    Status.APPLICATION,
                    status,
                    "application status"
            );
        } catch (IllegalArgumentException e) {
            return false;
        }

        return applicationDAO.updateApplicationStatus(
                applicationId,
                status
        );
    }
}