package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Status;
import model.User;

/**
 * Database operations for users.
 * Methods return data and throw SQLException on database errors
 * (e.g. duplicate email, foreign-key violation) - they never just print.
 * The password column is intentionally never returned.
 */
public class UserDAO {

    private static final String COLUMNS =
        "user_id, name, email, role, contact, address, city, state";

    private static User map(ResultSet r) throws SQLException {
        return new User(
            r.getInt("user_id"), r.getString("name"), r.getString("email"),
            r.getString("role"), r.getString("contact"), r.getString("address"),
            r.getString("city"), r.getString("state"));
    }

    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM Users ORDER BY user_id";
        List<User> users = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet r = ps.executeQuery()) {
            while (r.next()) {
                users.add(map(r));
            }
        }
        return users;
    }

    /** Returns the user, or null if no user has this id. */
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM Users WHERE user_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet r = ps.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    /**
     * Adds a user and returns the new user_id.
     * @throws IllegalArgumentException if role is not Admin/Shelter/Adopter
     * @throws SQLException e.g. SQLIntegrityConstraintViolationException for a duplicate email
     */
    public int addUser(String name, String email, String password, String role,
                       String contact, String address, String city, String state)
            throws SQLException {
        Status.require(Status.ROLES, role, "role");
        String sql = "INSERT INTO Users (name, email, password, role, contact, address, city, state) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.setString(5, contact);
            ps.setString(6, address);
            ps.setString(7, city);
            ps.setString(8, state);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    /** Returns true if a user was updated, false if no user has this id. */
    public boolean updateUser(int userId, String name, String email, String role,
                              String contact, String address, String city, String state)
            throws SQLException {
        Status.require(Status.ROLES, role, "role");
        String sql = "UPDATE Users SET name = ?, email = ?, role = ?, contact = ?, "
                   + "address = ?, city = ?, state = ? WHERE user_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, role);
            ps.setString(4, contact);
            ps.setString(5, address);
            ps.setString(6, city);
            ps.setString(7, state);
            ps.setInt(8, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Returns true if deleted, false if no such user.
     * @throws SQLException if the user still owns pets, applications or messages (foreign key)
     */
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }
}
