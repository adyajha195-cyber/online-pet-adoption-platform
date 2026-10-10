package backend;

import model.Pet;
import java.sql.SQLException;
import java.util.List;

public interface PetService {

    // Retrieve all pets
    List<Pet> getAllPets() throws SQLException;

    // Retrieve pets available for adoption
    List<Pet> getAvailablePets() throws SQLException;

    // Retrieve a pet by ID
    Pet getPetById(int petId) throws SQLException;

    // Retrieve pets belonging to a shelter
    List<Pet> getPetsByShelter(int shelterId) throws SQLException;

    // Search pets by type, breed, and city
    List<Pet> searchPets(String type, String breed, String city)
            throws SQLException;

    // Add a new pet listing
    int addPet(
            int shelterId,
            String name,
            String type,
            String breed,
            int age,
            String description,
            String photo
    ) throws SQLException;

    // Update an existing pet
    boolean updatePet(Pet pet) throws SQLException;

    // Approve or reject a pet listing
    boolean updatePetListingStatus(int petId, String status)
            throws SQLException;

    // Update a pet's availability status
    boolean updatePetStatus(int petId, String status)
            throws SQLException;

    // Delete a pet listing
    boolean deletePet(int petId) throws SQLException;
}
