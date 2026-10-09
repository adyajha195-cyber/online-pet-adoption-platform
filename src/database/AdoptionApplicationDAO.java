package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.AdoptionApplication;
import model.Status;

/** Database operations for adoption applications. Returns data / throws SQLException. */
public class AdoptionApplicationDAO {

    private static final String COLUMNS =
        "a.application_id, a.adopter_id, a.pet_id, a.application_details, a.application_date, a.status";

    private static AdoptionApplication map(ResultSet r) throws SQLException {
        return new AdoptionApplication(
            r.getInt("application_id"), r.getInt("adopter_id"), r.getInt("pet_id"),
            r.getString("application_details"), r.getString("application_date"),
            r.getString("status"));
    }

    private List<AdoptionApplication> query(String sql, int param) throws SQLException {
        List<AdoptionApplication> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (sql.contains("?")) {
                ps.setInt(1, param);
            }
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    list.add(map(r));
                }
            }
        }
        return list;
    }

    public List<AdoptionApplication> getAllApplications() throws SQLException {
        return query("SELECT " + COLUMNS + " FROM adoptionapplications a ORDER BY a.application_id", 0);
    }

    /** Returns the application, or null if no application has this id. */
    public AdoptionApplication getApplicationById(int applicationId) throws SQLException {
        List<AdoptionApplication> found = query(
            "SELECT " + COLUMNS + " FROM adoptionapplications a WHERE a.application_id = ?", applicationId);
        return found.isEmpty() ? null : found.get(0);
    }

    /** Adopter's "track my applications" / adoption history screen. */
    public List<AdoptionApplication> getApplicationsByAdopter(int adopterId) throws SQLException {
        return query("SELECT " + COLUMNS + " FROM adoptionapplications a "
                   + "WHERE a.adopter_id = ? ORDER BY a.application_id", adopterId);
    }

    /** Shelter's "view applications" screen: applications for pets this shelter owns. */
    public List<AdoptionApplication> getApplicationsByShelter(int shelterUserId) throws SQLException {
        return query("SELECT " + COLUMNS + " FROM adoptionapplications a "
                   + "JOIN pets p ON a.pet_id = p.pet_id "
                   + "WHERE p.shelter_user_id = ? ORDER BY a.application_id", shelterUserId);
    }

    /**
     * Submits an application. The rules are checked and the row inserted in ONE statement,
     * so two people clicking at once can't sneak past them:
     *   - the pet must exist, have listing_status Approved and pet_status Available
     *   - the same adopter can't apply for the same pet twice
     *
     * @return the new application_id, or -1 if a rule above blocked it
     * @throws SQLException e.g. if adopterId is not an existing user
     */
    public int addApplication(int adopterId, int petId, String applicationDetails) throws SQLException {
        String sql = "INSERT INTO adoptionapplications (adopter_id, pet_id, application_details) "
                   + "SELECT ?, p.pet_id, ? FROM pets p "
                   + "WHERE p.pet_id = ? AND p.listing_status = 'Approved' AND p.pet_status = 'Available' "
                   + "AND NOT EXISTS (SELECT 1 FROM adoptionapplications a "
                   + "                WHERE a.adopter_id = ? AND a.pet_id = p.pet_id)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, adopterId);
            ps.setString(2, applicationDetails);
            ps.setInt(3, petId);
            ps.setInt(4, adopterId);
            if (ps.executeUpdate() == 0) {
                return -1;
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    /**
     * @param status must be Pending, Approved or Rejected
     * @return true if updated, false if no such application
     * @throws IllegalArgumentException for any other status
     */
    public boolean updateApplicationStatus(int applicationId, String status) throws SQLException {
        Status.require(Status.APPLICATION, status, "application status");
        String sql = "UPDATE adoptionapplications SET status = ? WHERE application_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, applicationId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Returns true if deleted, false if no such application. */
    public boolean deleteApplication(int applicationId) throws SQLException {
        String sql = "DELETE FROM adoptionapplications WHERE application_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            return ps.executeUpdate() > 0;
        }
    }
}
