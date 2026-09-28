package database;

public class UserDAOTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        userDAO.getUserById(1);
    }
}