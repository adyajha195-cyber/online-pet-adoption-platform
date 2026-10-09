package database;

/** Quick manual check: prints what the DAO returns. The real assertions are in DaoIntegrationTest. */
public class UserDAOTest {

    public static void main(String[] args) throws Exception {
        System.out.println(new UserDAO().getUserById(1));
    }
}
