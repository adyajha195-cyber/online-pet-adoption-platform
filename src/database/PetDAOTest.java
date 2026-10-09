package database;

/** Quick manual check: prints what the DAO returns. The real assertions are in DaoIntegrationTest. */
public class PetDAOTest {

    public static void main(String[] args) throws Exception {
        System.out.println(new PetDAO().getPetById(1));
    }
}
