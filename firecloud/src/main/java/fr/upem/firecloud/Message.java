package fr.upem.firecloud;


import java.util.Map;

public class Message {

    private String from;
    private String messageId;

    private Map<String, String> payload;

    /**
     * Creates a new Message.
     * @param from the device from which the message has been received
     * @param messageId the message id
     * @param payload a Json map representing the payload
     */
    public Message(String from, String messageId, Map<String, String> payload) {
        this.from = from;
        this.messageId = messageId;
        this.payload = payload;
    }

    /**
     * Get the device from which the message has been send.
     * @return the device from which the message has been send
     */
    public String getFrom() {
        return from;
    }

    /**
     * Get the message id
     * @return the message id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Get the payload of the message.
     * @return a Json map representing the payload
     */
    public Map<String, String> getPayload() {
        return payload;
    }
}