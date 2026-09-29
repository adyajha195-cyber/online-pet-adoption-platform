package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Handles database operations for messages between users.
 */
public class MessageDAO {

    public void getAllMessages() {

        String sql = "SELECT * FROM Messages";

        try {
            Connection connection = DatabaseConnection.getConnection();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery();

            while (result.next()) {

                System.out.println(
                    result.getInt("message_id") + " | " +
                    result.getInt("sender_id") + " | " +
                    result.getInt("receiver_id") + " | " +
                    result.getString("message_text") + " | " +
                    result.getString("send_at") + " | " +
                    result.getString("delivery_status")
                );
            }

            result.close();
            statement.close();
            DatabaseConnection.closeConnection(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        public void getMessageById(int messageId) {

    String sql = "SELECT * FROM Messages WHERE message_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setInt(1, messageId);

        ResultSet result = statement.executeQuery();

        if (result.next()) {

            System.out.println(
                result.getInt("message_id") + " | " +
                result.getInt("sender_id") + " | " +
                result.getInt("receiver_id") + " | " +
                result.getString("message_text") + " | " +
                result.getString("send_at") + " | " +
                result.getString("delivery_status")
            );

        } else {
            System.out.println("Message not found.");
        }

        result.close();
        statement.close();
        DatabaseConnection.closeConnection(connection);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void addMessage(int senderId, int receiverId, String messageText) {

    String sql = "INSERT INTO Messages " +
                 "(sender_id, receiver_id, message_text) " +
                 "VALUES (?, ?, ?)";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement(sql);

        statement.setInt(1, senderId);
        statement.setInt(2, receiverId);
        statement.setString(3, messageText);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Message added successfully!");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}   public void updateDeliveryStatus(int messageId, String status) {

    String sql = "UPDATE Messages SET delivery_status = ? WHERE message_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, status);
        statement.setInt(2, messageId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Message delivery status updated successfully!");
        } else {
            System.out.println("Message not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}       
        public void deleteMessage(int messageId) {

    String sql = "DELETE FROM Messages WHERE message_id = ?";

    try {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, messageId);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println("Message deleted successfully!");
        } else {
            System.out.println("Message not found.");
        }

        statement.close();
        DatabaseConnection.closeConnection(connection);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}