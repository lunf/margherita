package lunf.queen.margherita.domain;

import lombok.Data;

import java.net.URL;
import java.time.Instant;

@Data
public class FbMessage {
    private String id;
    private String text;
    private String senderId;
    private Instant timestamp;
    private String recipientId;
    private String attachmentUrl;
    private String attachmentType;
    private double longitude;
    private double latitude;

    public FbMessage(NewMessageBuilder newMessageBuilder) {
        this.senderId = newMessageBuilder.senderId;
        this.timestamp = newMessageBuilder.timestamp;
        this.id = newMessageBuilder.messageId;
        this.text = newMessageBuilder.textMessage;
        this.recipientId = newMessageBuilder.recipientId;
        this.attachmentUrl = newMessageBuilder.attachmentUrl.toString();
        this.attachmentType = newMessageBuilder.attachmentType;
        this.longitude = newMessageBuilder.longitude;
        this.latitude = newMessageBuilder.latitude;
    }

    public static class NewMessageBuilder {
        private String senderId;
        private Instant timestamp;
        private String messageId;
        private String textMessage;
        private String recipientId;
        private URL attachmentUrl;
        private String attachmentType;
        private double longitude;
        private double latitude;

        public NewMessageBuilder(String senderId, Instant timestamp) {
            this.senderId = senderId;
            this.timestamp = timestamp;
        }

        public NewMessageBuilder setMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public NewMessageBuilder setTextMessage(String textMessage) {
            this.textMessage = textMessage;
            return this;
        }

        public NewMessageBuilder setRecipientId(String recipientId) {
            this.recipientId = recipientId;
            return this;
        }

        public NewMessageBuilder setAttachmentUrl(URL attachmentUrl) {
            this.attachmentUrl = attachmentUrl;
            return this;
        }

        public NewMessageBuilder setAttachmentType(String attachmentType) {
            this.attachmentType = attachmentType;
            return this;
        }

        public NewMessageBuilder setLocation(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
            return this;
        }

        public FbMessage build() {
            FbMessage fbMessage = new FbMessage(this);

            return fbMessage;
        }
    }
}
