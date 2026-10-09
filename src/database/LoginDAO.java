package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

/** Checks an email + password against the Users table. */
public class LoginDAO {

    /** Returns the logged-in user, or null if the email/password is wrong. */
    public User login(String email, String password) throws SQLException {
        String sql = "SELECT user_id, name, email, role, contact, address, city, state "
                   + "FROM Users WHERE email = ? AND password = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet r = ps.executeQuery()) {
                if (!r.next()) {
                    return null;
                }
                return new User(
                    r.getInt("user_id"), r.getString("name"), r.getString("email"),
                    r.getString("role"), r.getString("contact"), r.getString("address"),
                    r.getString("city"), r.getString("state"));
            }
        }
    }

    /** Kept for the old backend code: returns {user_id, name, role} or null. Prefer login(). */
    public Object[] getLoggedInUser(String email, String password) throws SQLException {
        User u = login(email, password);
        return (u == null) ? null : new Object[] { u.getUserId(), u.getName(), u.getRole() };
    }
}
