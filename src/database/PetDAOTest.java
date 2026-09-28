package database;

public class PetDAOTest {

    public static void main(String[] args) {

        PetDAO petDAO = new PetDAO();

        petDAO.getPetById(1);
    }
}