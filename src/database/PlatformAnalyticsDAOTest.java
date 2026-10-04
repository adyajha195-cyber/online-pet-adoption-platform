package database;

public class PlatformAnalyticsDAOTest {

    public static void main(String[] args) {

        PlatformAnalyticsDAO analytics = new PlatformAnalyticsDAO();

        System.out.println("Total Users: " + analytics.getTotalUsers());
        System.out.println("Total Pets: " + analytics.getTotalPets());
        System.out.println("Total Applications: " + analytics.getTotalApplications());
        System.out.println("Approved Applications: " + analytics.getApprovedApplications());
    }
}