package database;

import java.sql.SQLException;

public class MessageDAOTest {

    public static void main(String[] args) {
        MessageDAO messageDAO = new MessageDAO();

        try {
            messageDAO.updateDeliveryStatus(3, "Delivered");
            System.out.println("Delivery status updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
