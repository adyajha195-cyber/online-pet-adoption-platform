package database;

public class MessageDAOTest {

    public static void main(String[] args) {

        MessageDAO messageDAO = new MessageDAO();

        messageDAO.getMessageById(1);
    }
}