package model;

/** Plain data object for one row of the Message table. Immutable. */
public class Message {

    private final int messageId;
    private final int senderId;
    private final int receiverId;
    private final String messageText;
    private final String sentAt;
    private final String deliveryStatus;

    public Message(int messageId, int senderId, int receiverId, String messageText, String sentAt, String deliveryStatus) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.sentAt = sentAt;
        this.deliveryStatus = deliveryStatus;
    }

    public int getMessageId() { return messageId; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getMessageText() { return messageText; }
    public String getSentAt() { return sentAt; }
    public String getDeliveryStatus() { return deliveryStatus; }

    @Override
    public String toString() {
        return messageId + " | " + senderId + " | " + receiverId + " | " + messageText + " | " + sentAt + " | " + deliveryStatus;
    }
}
