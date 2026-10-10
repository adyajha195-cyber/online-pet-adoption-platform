package backend;

import database.PetDAO;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import model.Pet;
import model.Status;

public class PetServiceImpl implements PetService {

    private final PetDAO petDAO;

    public PetServiceImpl() {
        this.petDAO = new PetDAO();
    }

    @Override
    public List<Pet> getAllPets() throws SQLException {
        return petDAO.getAllPets();
    }

    @Override
    public List<Pet> getAvailablePets() throws SQLException {
        return petDAO.getAvailablePets();
    }

    @Override
    public Pet getPetById(int petId) throws SQLException {

        if (petId <= 0) {
            return null;
        }

        return petDAO.getPetById(petId);
    }

    @Override
    public List<Pet> getPetsByShelter(int shelterId)
            throws SQLException {

        if (shelterId <= 0) {
            throw new IllegalArgumentException("Invalid shelter ID.");
        }

        return petDAO.getPetsByShelter(shelterId);
    }

    @Override
    public List<Pet> searchPets(
            String type,
            String breed,
            String city
    ) throws SQLException {

        return petDAO.searchPets(type, breed, city);
    }

    @Override
    public int addPet(
            int shelterId,
            String name,
            String type,
            String breed,
            int age,
            String description,
            String photo
    ) throws SQLException {

        if (shelterId <= 0) {
            throw new IllegalArgumentException("Invalid shelter ID.");
        }

        validatePetFields(name, type, breed, age);

        return petDAO.addPet(
                shelterId,
                name.trim(),
                type.trim(),
                breed.trim(),
                age,
                description,
                photo
        );
    }

    @Override
    public boolean updatePet(Pet pet) throws SQLException {

        if (pet == null) {
            throw new IllegalArgumentException("Pet cannot be null.");
        }

        if (pet.getPetId() <= 0) {
            throw new IllegalArgumentException("Invalid pet ID.");
        }

        validatePetFields(
                pet.getName(),
                pet.getType(),
                pet.getBreed(),
                pet.getAge()
        );

        // PetDAO.updatePet takes individual fields, not a Pet object.
        // Listing/pet status are changed only through their own methods.
        return petDAO.updatePet(
                pet.getPetId(),
                pet.getName().trim(),
                pet.getType().trim(),
                pet.getBreed().trim(),
                pet.getAge(),
                pet.getDescription(),
                pet.getPhoto()
        );
    }

    @Override
    public boolean updatePetListingStatus(
            int petId,
            String status
    ) throws SQLException {

        if (petId <= 0) {
            throw new IllegalArgumentException("Invalid pet ID.");
        }

        Status.require(Status.LISTING, status, "listing status");

        return petDAO.updatePetListingStatus(petId, status);
    }

    @Override
    public boolean updatePetStatus(
            int petId,
            String status
    ) throws SQLException {

        if (petId <= 0) {
            throw new IllegalArgumentException("Invalid pet ID.");
        }

        Status.require(Status.PET, status, "pet status");

        return petDAO.updatePetStatus(petId, status);
    }

    @Override
    public boolean deletePet(int petId) throws SQLException {

        if (petId <= 0) {
            throw new IllegalArgumentException("Invalid pet ID.");
        }

        try {
            return petDAO.deletePet(petId);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException(
                    "This pet has adoption applications and cannot be "
                            + "deleted.", e
            );
        }
    }

    // Shared validation for addPet / updatePet
    private void validatePetFields(
            String name,
            String type,
            String breed,
            int age
    ) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be empty.");
        }

        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet type cannot be empty.");
        }

        if (breed == null || breed.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet breed cannot be empty.");
        }

        if (age < 0) {
            throw new IllegalArgumentException(
                    "Pet age cannot be negative."
            );
        }
    }
}
