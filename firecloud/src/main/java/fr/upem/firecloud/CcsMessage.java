package fr.upem.firecloud;


import java.util.Map;

/**
 * Represents a message for CCS based massaging.
 */
public class CcsMessage {

    /**
     * Recipient-ID.
     */
    private String from;
    /**
     * Sender app's package.
     */
    private String category;
    /**
     * Unique id for this message.
     */
    private String messageId;
    /**
     * Payload data. A String in Json format.
     */
    private Map<String, String> payload;

    public CcsMessage(String from, String category, String messageId, Map<String, String> payload) {
        this.from = from;
        this.category = category;
        this.messageId = messageId;
        this.payload = payload;
    }

    public String getFrom() {
        return from;
    }

    public String getCategory() {
        return category;
    }

    public String getMessageId() {
        return messageId;
    }

    public Map<String, String> getPayload() {
        return payload;
    }
}
