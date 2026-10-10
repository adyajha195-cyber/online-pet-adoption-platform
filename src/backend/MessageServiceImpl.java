package backend;

import database.MessageDAO;
import model.Message;
import model.Status;

import java.sql.SQLException;
import java.util.List;

public class MessageServiceImpl implements MessageService {

    private final MessageDAO messageDAO;

    public MessageServiceImpl() {
        this.messageDAO = new MessageDAO();
    }

    @Override
    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    @Override
    public Message getMessageById(int messageId) throws SQLException {

        if (messageId <= 0) {
            return null;
        }

        return messageDAO.getMessageById(messageId);
    }

    @Override
    public List<Message> getConversation(int userA, int userB)
            throws SQLException {

        if (userA <= 0 || userB <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID."
            );
        }

        return messageDAO.getConversation(userA, userB);
    }

    @Override
    public int sendMessage(
            int senderId,
            int receiverId,
            String messageText
    ) throws SQLException {

        if (senderId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid sender ID."
            );
        }

        if (receiverId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid receiver ID."
            );
        }

        if (senderId == receiverId) {
            throw new IllegalArgumentException(
                    "You cannot send a message to yourself."
            );
        }

        if (messageText == null || messageText.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Message cannot be empty."
            );
        }

        return messageDAO.addMessage(
                senderId,
                receiverId,
                messageText.trim()
        );
    }

    @Override
    public boolean updateDeliveryStatus(
            int messageId,
            String status
    ) throws SQLException {

        if (messageId <= 0 || status == null) {
            return false;
        }

        try {
            Status.require(
                    Status.DELIVERY,
                    status,
                    "delivery status"
            );
        } catch (IllegalArgumentException e) {
            return false;
        }

        return messageDAO.updateDeliveryStatus(
                messageId,
                status
        );
    }

    @Override
    public boolean deleteMessage(int messageId) throws SQLException {

        if (messageId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid message ID."
            );
        }

        return messageDAO.deleteMessage(messageId);
    }
}
