package database;

public class LoginDAOTest {

    public static void main(String[] args) {

        LoginDAO loginDAO = new LoginDAO();

       loginDAO.login(
        "updated@petadoption.com",
        "adopter123"
);
    }
}