package backend;

import java.sql.SQLException;

public interface PlatformAnalyticsService {

    int getTotalUsers() throws SQLException;

    int getTotalPets() throws SQLException;

    int getTotalApplications() throws SQLException;

    int getApprovedApplications() throws SQLException;
}
