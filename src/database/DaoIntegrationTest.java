package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import backend.AdoptionApplicationService;
import backend.AdoptionApplicationServiceImpl;
import model.AdoptionApplication;
import model.Message;
import model.Pet;
import model.Setting;
import model.Status;
import model.User;

/**
 * Assertion-based integration test for the DAO + service layers.
 *
 * Run it against a freshly created database (run sql/pet_online_adoption.sql first).
 * It cleans up after itself, so the sample data is left as it was found.
 * Exit code is 0 if everything passes, 1 otherwise.
 */
public class DaoIntegrationTest {

    private static int passed = 0;
    private static int failed = 0;

    private interface Action { void run() throws Exception; }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  PASS  " + name);
        } else {
            failed++;
            System.out.println("  FAIL  " + name);
        }
    }

    /** True if the action throws an exception of the given type (or a subclass). */
    private static boolean throwsA(Class<? extends Throwable> type, Action action) {
        try {
            action.run();
            return false;
        } catch (Throwable t) {
            return type.isInstance(t);
        }
    }

    /** Count rows with raw JDBC, independent of the DAOs under test. */
    private static int count(String sql, Object... params) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet r = ps.executeQuery()) {
                r.next();
                return r.getInt(1);
            }
        }
    }

    private static boolean hasPet(List<Pet> pets, int petId) {
        for (Pet p : pets) {
            if (p.getPetId() == petId) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {

        UserDAO userDAO = new UserDAO();
        PetDAO petDAO = new PetDAO();
        AdoptionApplicationDAO appDAO = new AdoptionApplicationDAO();
        MessageDAO msgDAO = new MessageDAO();
        SettingsDAO settingsDAO = new SettingsDAO();
        LoginDAO loginDAO = new LoginDAO();
        PlatformAnalyticsDAO analytics = new PlatformAnalyticsDAO();
        AdoptionApplicationService service = new AdoptionApplicationServiceImpl();

        System.out.println("\n[Connection + seed data]");
        try (Connection c = DatabaseConnection.getConnection()) {
            check("can connect to pet_adoption", c != null && !c.isClosed());
        }
        check("seed: 3 users, 2 pets, 1 application",
              analytics.getTotalUsers() == 3 && analytics.getTotalPets() == 2
              && analytics.getTotalApplications() == 1);
        boolean rolesOk = true;
        for (User u : userDAO.getAllUsers()) {
            rolesOk &= Status.ROLES.contains(u.getRole());
        }
        check("seed: every user has a valid role (Admin/Shelter/Adopter)", rolesOk);

        // ---------------- users ----------------
        System.out.println("\n[UserDAO]");
        int userId = userDAO.addUser("Test User", "test.user@x.com", "pw123", "Adopter",
                "9999999999", "1 Test St", "Delhi", "Delhi");
        check("addUser returns the new id", userId > 3);
        User u = userDAO.getUserById(userId);
        check("getUserById returns the saved fields",
              u != null && "Test User".equals(u.getName()) && "Adopter".equals(u.getRole())
              && "Delhi".equals(u.getCity()));
        check("getUserById for a missing id returns null", userDAO.getUserById(99999) == null);
        check("getAllUsers includes the new user", userDAO.getAllUsers().size() == 4);
        check("duplicate email throws SQLException (and adds no row)",
              throwsA(SQLException.class, () -> userDAO.addUser("Dup", "test.user@x.com", "p", "Adopter", "1", "a", "b", "c"))
              && count("SELECT COUNT(*) FROM Users WHERE email=?", "test.user@x.com") == 1);
        check("invalid role throws IllegalArgumentException",
              throwsA(IllegalArgumentException.class, () -> userDAO.addUser("X", "x@x.com", "p", "Superuser", "1", "a", "b", "c")));
        check("updateUser returns true and saves changes",
              userDAO.updateUser(userId, "Renamed", "test.user@x.com", "Adopter", "8", "2 St", "Noida", "UP")
              && "Renamed".equals(userDAO.getUserById(userId).getName()));
        check("updateUser on a missing id returns false",
              !userDAO.updateUser(99999, "n", "e@e.com", "Adopter", "1", "a", "b", "c"));

        // ---------------- login ----------------
        System.out.println("\n[LoginDAO]");
        User in = loginDAO.login("shelter@petadoption.com", "shelter123");
        check("login with valid credentials returns the User (role Shelter)",
              in != null && in.getUserId() == 2 && Status.SHELTER.equals(in.getRole()));
        check("login with wrong password returns null", loginDAO.login("shelter@petadoption.com", "WRONG") == null);
        check("login with unknown email returns null", loginDAO.login("nobody@x.com", "x") == null);
        check("SQL-injection string does not log in", loginDAO.login("' OR '1'='1", "' OR '1'='1") == null);
        Object[] old = loginDAO.getLoggedInUser("adopter@petadoption.com", "adopter123");
        check("old getLoggedInUser still returns {id, name, role}",
              old != null && old.length == 3 && Integer.valueOf(3).equals(old[0]));

        // ---------------- pets ----------------
        System.out.println("\n[PetDAO]");
        int petId = petDAO.addPet(2, "TestPet", "Dog", "Beagle", 4, "desc", "images/t.jpg");
        Pet pet = petDAO.getPetById(petId);
        check("addPet + getPetById round-trip", pet != null && "Beagle".equals(pet.getBreed()) && pet.getAge() == 4);
        check("new pet starts as listing Pending / pet Available",
              Status.PENDING.equals(pet.getListingStatus()) && Status.AVAILABLE.equals(pet.getPetStatus()));
        check("pending pet is NOT browsable", !hasPet(petDAO.getAvailablePets(), petId));
        check("getPetsByShelter(2) includes it", hasPet(petDAO.getPetsByShelter(2), petId));
        check("updatePet returns true and saves",
              petDAO.updatePet(petId, "TestPet2", "Dog", "Poodle", 5, "d2", "p2")
              && "Poodle".equals(petDAO.getPetById(petId).getBreed()));
        check("updatePetListingStatus Approved works and makes it browsable",
              petDAO.updatePetListingStatus(petId, Status.APPROVED) && hasPet(petDAO.getAvailablePets(), petId));
        check("invalid listing status throws IllegalArgumentException",
              throwsA(IllegalArgumentException.class, () -> petDAO.updatePetListingStatus(petId, "Maybe")));
        check("searchPets by type+breed finds it", hasPet(petDAO.searchPets("Dog", "Poodle", null), petId));
        check("searchPets by shelter city finds it (shelter is in Noida)", hasPet(petDAO.searchPets(null, null, "Noida"), petId));
        check("searchPets with a non-matching city excludes it", !hasPet(petDAO.searchPets(null, null, "Mumbai"), petId));
        check("addPet with a non-existent shelter throws SQLException",
              throwsA(SQLException.class, () -> petDAO.addPet(9999, "Ghost", "Cat", "x", 1, "d", "p")));
        check("updatePetStatus Adopted removes it from browsing",
              petDAO.updatePetStatus(petId, Status.ADOPTED) && !hasPet(petDAO.getAvailablePets(), petId));
        petDAO.updatePetStatus(petId, Status.AVAILABLE);

        // ---------------- applications ----------------
        System.out.println("\n[AdoptionApplicationDAO]");
        int appId = appDAO.addApplication(userId, petId, "please let me adopt");
        AdoptionApplication app = appDAO.getApplicationById(appId);
        check("addApplication returns an id and starts Pending", appId > 0 && app != null && Status.PENDING.equals(app.getStatus()));
        check("duplicate application is blocked (-1)", appDAO.addApplication(userId, petId, "again") == -1);
        int pendingPet = petDAO.addPet(2, "PendingPet", "Cat", "x", 1, "d", "p");
        check("application for a not-Approved listing is blocked (-1)", appDAO.addApplication(userId, pendingPet, "x") == -1);
        check("application for a non-existent pet is blocked (-1)", appDAO.addApplication(userId, 99999, "x") == -1);
        check("application from a non-existent adopter throws SQLException",
              throwsA(SQLException.class, () -> appDAO.addApplication(99999, petId, "x")));
        check("getApplicationsByAdopter returns exactly this adopter's application",
              appDAO.getApplicationsByAdopter(userId).size() == 1);
        boolean shelterSees = false;
        for (AdoptionApplication a : appDAO.getApplicationsByShelter(2)) {
            shelterSees |= (a.getApplicationId() == appId);
        }
        check("getApplicationsByShelter shows it to the owning shelter", shelterSees);
        check("updateApplicationStatus Approved returns true",
              appDAO.updateApplicationStatus(appId, Status.APPROVED)
              && Status.APPROVED.equals(appDAO.getApplicationById(appId).getStatus()));
        check("analytics counts the approved application", analytics.getApprovedApplications() == 1);
        check("invalid application status throws IllegalArgumentException",
              throwsA(IllegalArgumentException.class, () -> appDAO.updateApplicationStatus(appId, "BananaStatus")));
        check("status unchanged after the invalid attempt", Status.APPROVED.equals(appDAO.getApplicationById(appId).getStatus()));
        check("updateApplicationStatus on a missing id returns false", !appDAO.updateApplicationStatus(99999, Status.APPROVED));

        // ---------------- messages ----------------
        System.out.println("\n[MessageDAO]");
        int msgId = msgDAO.addMessage(userId, 2, "hello shelter");
        Message m = msgDAO.getMessageById(msgId);
        check("addMessage round-trip, status starts Sent", m != null && "hello shelter".equals(m.getMessageText()) && Status.SENT.equals(m.getDeliveryStatus()));
        check("getConversation(user, shelter) contains it, in both argument orders",
              msgDAO.getConversation(userId, 2).size() == 1 && msgDAO.getConversation(2, userId).size() == 1);
        check("seed conversation between users 2 and 3 has 2 messages", msgDAO.getConversation(2, 3).size() == 2);
        check("updateDeliveryStatus Read works",
              msgDAO.updateDeliveryStatus(msgId, Status.READ) && Status.READ.equals(msgDAO.getMessageById(msgId).getDeliveryStatus()));
        check("invalid delivery status throws IllegalArgumentException",
              throwsA(IllegalArgumentException.class, () -> msgDAO.updateDeliveryStatus(msgId, "Teleported")));
        check("message to a non-existent user throws SQLException",
              throwsA(SQLException.class, () -> msgDAO.addMessage(userId, 99999, "x")));

        // ---------------- settings ----------------
        System.out.println("\n[SettingsDAO]");
        check("seed MaintenanceMode is OFF", "OFF".equals(settingsDAO.getSettingValue("MaintenanceMode")));
        int setId = settingsDAO.addSetting("TestSetting", "A");
        check("addSetting round-trip", "A".equals(settingsDAO.getSettingValue("TestSetting")));
        check("duplicate setting name throws SQLException", throwsA(SQLException.class, () -> settingsDAO.addSetting("TestSetting", "B")));
        Setting s = settingsDAO.getSettingById(setId);
        check("getSettingById returns it", s != null && "TestSetting".equals(s.getName()));
        check("updateSetting changes the value",
              settingsDAO.updateSetting(setId, "TestSetting", "B") && "B".equals(settingsDAO.getSettingValue("TestSetting")));
        check("getSettingValue for a missing name returns null", settingsDAO.getSettingValue("Nope") == null);

        // ---------------- service layer ----------------
        System.out.println("\n[AdoptionApplicationService]");
        int pet2 = petDAO.addPet(2, "ServicePet", "Cat", "Siamese", 2, "d", "p");
        petDAO.updatePetListingStatus(pet2, Status.APPROVED);
        check("submitApplication(adopter, pet) returns true for an approved pet", service.submitApplication(userId, pet2));
        check("submitting the same application again returns false", !service.submitApplication(userId, pet2));
        check("submitting for an unapproved pet returns false", !service.submitApplication(userId, pendingPet, "x"));
        int svcApp = appDAO.getApplicationsByAdopter(userId).stream()
                .filter(a -> a.getPetId() == pet2).findFirst().get().getApplicationId();
        check("getApplicationStatus returns Pending", Status.PENDING.equals(service.getApplicationStatus(svcApp)));
        check("updateApplicationStatus Rejected returns true and is readable back",
              service.updateApplicationStatus(svcApp, Status.REJECTED) && Status.REJECTED.equals(service.getApplicationStatus(svcApp)));
        check("updateApplicationStatus with an invalid status returns false", !service.updateApplicationStatus(svcApp, "Banana"));
        check("getApplicationStatus for a missing application returns null", service.getApplicationStatus(99999) == null);
        check("service throws ServiceException (not silent) for a non-existent adopter",
              throwsA(backend.ServiceException.class, () -> service.submitApplication(99999, pet2)));

        // ---------------- foreign keys on delete ----------------
        System.out.println("\n[Deletes / foreign keys]");
        check("deletePet throws SQLException while an application references it, row stays",
              throwsA(SQLException.class, () -> petDAO.deletePet(petId)) && petDAO.getPetById(petId) != null);
        check("deleteUser throws SQLException while the user has applications, row stays",
              throwsA(SQLException.class, () -> userDAO.deleteUser(userId)) && userDAO.getUserById(userId) != null);
        check("delete of a missing row returns false", !petDAO.deletePet(99999) && !appDAO.deleteApplication(99999));

        // ---------------- cleanup (FK-safe order) ----------------
        msgDAO.deleteMessage(msgId);
        appDAO.deleteApplication(appId);
        appDAO.deleteApplication(svcApp);
        petDAO.deletePet(petId);
        petDAO.deletePet(pet2);
        petDAO.deletePet(pendingPet);
        settingsDAO.deleteSetting(setId);
        userDAO.deleteUser(userId);
        check("cleanup restored the seed data",
              analytics.getTotalUsers() == 3 && analytics.getTotalPets() == 2
              && analytics.getTotalApplications() == 1 && settingsDAO.getAllSettings().size() == 3);

        System.out.println("\n==================================");
        System.out.println("Passed: " + passed + "   Failed: " + failed);
        System.out.println("==================================");
        System.exit(failed == 0 ? 0 : 1);
    }
}
