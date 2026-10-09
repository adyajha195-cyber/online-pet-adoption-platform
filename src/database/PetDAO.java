package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Pet;
import model.Status;

/** Database operations for pets. Returns data / throws SQLException - never just prints. */
public class PetDAO {

    private static final String COLUMNS =
        "p.pet_id, p.shelter_user_id, p.name, p.type, p.breed, p.age, "
      + "p.description, p.photo, p.listing_status, p.pet_status";

    private static Pet map(ResultSet r) throws SQLException {
        return new Pet(
            r.getInt("pet_id"), r.getInt("shelter_user_id"), r.getString("name"),
            r.getString("type"), r.getString("breed"), r.getInt("age"),
            r.getString("description"), r.getString("photo"),
            r.getString("listing_status"), r.getString("pet_status"));
    }

    private List<Pet> query(String sql, List<Object> params) throws SQLException {
        List<Pet> pets = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    pets.add(map(r));
                }
            }
        }
        return pets;
    }

    /** Every pet, any status (for admin screens). */
    public List<Pet> getAllPets() throws SQLException {
        return query("SELECT " + COLUMNS + " FROM Pets p ORDER BY p.pet_id", new ArrayList<>());
    }

    /** Pets an adopter may browse: listing Approved and pet Available. */
    public List<Pet> getAvailablePets() throws SQLException {
        return query("SELECT " + COLUMNS + " FROM Pets p "
                   + "WHERE p.listing_status = 'Approved' AND p.pet_status = 'Available' "
                   + "ORDER BY p.pet_id", new ArrayList<>());
    }

    /** All pets belonging to one shelter (any status). */
    public List<Pet> getPetsByShelter(int shelterUserId) throws SQLException {
        List<Object> params = new ArrayList<>();
        params.add(shelterUserId);
        return query("SELECT " + COLUMNS + " FROM Pets p WHERE p.shelter_user_id = ? ORDER BY p.pet_id", params);
    }

    /**
     * Adopter search over browsable pets. Pass null or "" for any filter you don't want.
     * type = exact match, breed = contains, city = the shelter's city.
     */
    public List<Pet> searchPets(String type, String breed, String city) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT " + COLUMNS + " FROM Pets p JOIN Users u ON p.shelter_user_id = u.user_id "
          + "WHERE p.listing_status = 'Approved' AND p.pet_status = 'Available'");
        List<Object> params = new ArrayList<>();
        if (type != null && !type.isBlank()) {
            sql.append(" AND p.type = ?");
            params.add(type.trim());
        }
        if (breed != null && !breed.isBlank()) {
            sql.append(" AND p.breed LIKE ?");
            params.add("%" + breed.trim() + "%");
        }
        if (city != null && !city.isBlank()) {
            sql.append(" AND u.city = ?");
            params.add(city.trim());
        }
        sql.append(" ORDER BY p.pet_id");
        return query(sql.toString(), params);
    }

    /** Returns the pet, or null if no pet has this id. */
    public Pet getPetById(int petId) throws SQLException {
        List<Object> params = new ArrayList<>();
        params.add(petId);
        List<Pet> found = query("SELECT " + COLUMNS + " FROM Pets p WHERE p.pet_id = ?", params);
        return found.isEmpty() ? null : found.get(0);
    }

    /**
     * Adds a pet (listing_status starts as Pending until an admin approves it)
     * and returns the new pet_id.
     * @throws SQLException e.g. if shelterUserId is not an existing user
     */
    public int addPet(int shelterUserId, String name, String type, String breed,
                      int age, String description, String photo) throws SQLException {
        String sql = "INSERT INTO Pets (shelter_user_id, name, type, breed, age, description, photo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, shelterUserId);
            ps.setString(2, name);
            ps.setString(3, type);
            ps.setString(4, breed);
            ps.setInt(5, age);
            ps.setString(6, description);
            ps.setString(7, photo);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    /** Returns true if updated, false if no such pet. */
    public boolean updatePet(int petId, String name, String type, String breed,
                             int age, String description, String photo) throws SQLException {
        String sql = "UPDATE Pets SET name = ?, type = ?, breed = ?, age = ?, "
                   + "description = ?, photo = ? WHERE pet_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, breed);
            ps.setInt(4, age);
            ps.setString(5, description);
            ps.setString(6, photo);
            ps.setInt(7, petId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Admin approves/rejects a listing. status must be Pending, Approved or Rejected. */
    public boolean updatePetListingStatus(int petId, String status) throws SQLException {
        Status.require(Status.LISTING, status, "listing status");
        return updateColumn("listing_status", petId, status);
    }

    /** Marks a pet Available or Adopted. */
    public boolean updatePetStatus(int petId, String status) throws SQLException {
        Status.require(Status.PET, status, "pet status");
        return updateColumn("pet_status", petId, status);
    }

    private boolean updateColumn(String column, int petId, String value) throws SQLException {
        String sql = "UPDATE Pets SET " + column + " = ? WHERE pet_id = ?"; // column is a fixed constant above
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setInt(2, petId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Returns true if deleted, false if no such pet.
     * @throws SQLException if applications still reference the pet (foreign key)
     */
    public boolean deletePet(int petId) throws SQLException {
        String sql = "DELETE FROM Pets WHERE pet_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, petId);
            return ps.executeUpdate() > 0;
        }
    }
}
