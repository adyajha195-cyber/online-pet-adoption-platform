package backend;

import database.PlatformAnalyticsDAO;
import java.sql.SQLException;

public class PlatformAnalyticsServiceImpl
        implements PlatformAnalyticsService {

    private final PlatformAnalyticsDAO analyticsDAO;

    public PlatformAnalyticsServiceImpl() {
        this.analyticsDAO = new PlatformAnalyticsDAO();
    }

    @Override
    public int getTotalUsers() throws SQLException {
        return analyticsDAO.getTotalUsers();
    }

    @Override
    public int getTotalPets() throws SQLException {
        return analyticsDAO.getTotalPets();
    }

    @Override
    public int getTotalApplications() throws SQLException {
        return analyticsDAO.getTotalApplications();
    }

    @Override
    public int getApprovedApplications() throws SQLException {
        return analyticsDAO.getApprovedApplications();
    }
}