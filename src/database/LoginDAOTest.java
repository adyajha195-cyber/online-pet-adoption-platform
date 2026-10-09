package database;

/** Quick manual check: prints what the DAO returns. The real assertions are in DaoIntegrationTest. */
public class LoginDAOTest {

    public static void main(String[] args) throws Exception {
        System.out.println(new LoginDAO().login("adopter@petadoption.com", "adopter123"));
    }
}
