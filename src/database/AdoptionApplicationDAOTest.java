package database;

public class AdoptionApplicationDAOTest {

    public static void main(String[] args) {

        AdoptionApplicationDAO applicationDAO =
            new AdoptionApplicationDAO();

        applicationDAO.getApplicationById(1);
    }
}