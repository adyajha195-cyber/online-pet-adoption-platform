package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Simple counts for the admin / shelter statistics screens. Throws SQLException on DB errors. */
public class PlatformAnalyticsDAO {

    private int count(String sql) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet r = ps.executeQuery()) {
            return r.next() ? r.getInt(1) : 0;
        }
    }

    public int getTotalUsers() throws SQLException {
        return count("SELECT COUNT(*) FROM Users");
    }

    public int getTotalPets() throws SQLException {
        return count("SELECT COUNT(*) FROM Pets");
    }

    public int getTotalApplications() throws SQLException {
        return count("SELECT COUNT(*) FROM AdoptionApplications");
    }

    public int getApprovedApplications() throws SQLException {
        return count("SELECT COUNT(*) FROM AdoptionApplications WHERE status = 'Approved'");
    }
}
