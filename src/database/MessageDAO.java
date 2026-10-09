package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Message;
import model.Status;

/** Database operations for messages between shelters and adopters. */
public class MessageDAO {

    private static final String COLUMNS =
        "message_id, sender_id, receiver_id, message_text, send_at, delivery_status";

    private static Message map(ResultSet r) throws SQLException {
        return new Message(
            r.getInt("message_id"), r.getInt("sender_id"), r.getInt("receiver_id"),
            r.getString("message_text"), r.getString("send_at"), r.getString("delivery_status"));
    }

    public List<Message> getAllMessages() throws SQLException {
        List<Message> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUMNS + " FROM Messages ORDER BY message_id");
             ResultSet r = ps.executeQuery()) {
            while (r.next()) {
                list.add(map(r));
            }
        }
        return list;
    }

    /** Returns the message, or null if no message has this id. */
    public Message getMessageById(int messageId) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT " + COLUMNS + " FROM Messages WHERE message_id = ?")) {
            ps.setInt(1, messageId);
            try (ResultSet r = ps.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        }
    }

    /** The chat between two users, oldest message first. */
    public List<Message> getConversation(int userA, int userB) throws SQLException {
        String sql = "SELECT " + COLUMNS + " FROM Messages "
                   + "WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) "
                   + "ORDER BY send_at, message_id";
        List<Message> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userA);
            ps.setInt(2, userB);
            ps.setInt(3, userB);
            ps.setInt(4, userA);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    list.add(map(r));
                }
            }
        }
        return list;
    }

    /**
     * Sends a message and returns the new message_id.
     * @throws SQLException if sender or receiver is not an existing user
     */
    public int addMessage(int senderId, int receiverId, String messageText) throws SQLException {
        String sql = "INSERT INTO Messages (sender_id, receiver_id, message_text) VALUES (?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, senderId);
            ps.setInt(2, receiverId);
            ps.setString(3, messageText);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    /** status must be Sent, Delivered or Read. Returns true if updated. */
    public boolean updateDeliveryStatus(int messageId, String status) throws SQLException {
        Status.require(Status.DELIVERY, status, "delivery status");
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE Messages SET delivery_status = ? WHERE message_id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, messageId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMessage(int messageId) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM Messages WHERE message_id = ?")) {
            ps.setInt(1, messageId);
            return ps.executeUpdate() > 0;
        }
    }
}
