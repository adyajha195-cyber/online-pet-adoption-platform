package backend;

import model.Message;
import java.sql.SQLException;
import java.util.List;

public interface MessageService {

    List<Message> getAllMessages() throws SQLException;

    Message getMessageById(int messageId) throws SQLException;

    List<Message> getConversation(int userA, int userB)
            throws SQLException;

    int sendMessage(int senderId, int receiverId, String messageText)
            throws SQLException;

    boolean updateDeliveryStatus(int messageId, String status)
            throws SQLException;

    boolean deleteMessage(int messageId) throws SQLException;
}