package lunf.queen.margherita.controllers;

import com.github.messenger4j.Messenger;
import com.github.messenger4j.exception.MessengerVerificationException;
import com.github.messenger4j.webhook.event.AttachmentMessageEvent;
import com.github.messenger4j.webhook.event.TextMessageEvent;
import com.github.messenger4j.webhook.event.attachment.Attachment;
import com.github.messenger4j.webhook.event.attachment.LocationAttachment;
import com.github.messenger4j.webhook.event.attachment.RichMediaAttachment;
import lunf.queen.margherita.business.InboundMessageBusiness;
import lunf.queen.margherita.domain.FbMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private Messenger messenger;

    @Autowired
    private InboundMessageBusiness inboundMessageBusiness;

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    /**
     * Webhook verification endpoint.
     *
     * The passed verification token (as query parameter) must match the configured verification token.
     * In case this is true, the passed challenge string must be returned by this endpoint.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") final String mode,
                                                @RequestParam("hub.verify_token") final String verifyToken,
                                                @RequestParam("hub.challenge") final String challenge) {

        logger.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode,
                verifyToken, challenge);
        try {
            this.messenger.verifyWebhook(mode, verifyToken);
            // Responds with the challenge token from the request
            return ResponseEntity.ok(challenge);
        } catch (MessengerVerificationException e) {
            logger.warn("Webhook verification failed: {}", e.getMessage());
            // Responds with '403 Forbidden' if verify tokens do not match
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    /**
     * Callback endpoint responsible for processing the inbound messages and events.
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleCallback(@RequestBody final String payload,
                                               @RequestHeader("X-Hub-Signature") final String signature) {

        logger.debug("Received Messenger Platform callback - payload: {} | signature: {}", payload, signature);
        try {
            this.messenger.onReceiveEvents(payload, Optional.of(signature), event -> {
                final String senderId = event.senderId();
                final String recipientId = event.recipientId();
                final Instant timestamp = event.timestamp();

                logger.debug("Received event from '{}' at '{}'", senderId, timestamp);

                if (event.isTextMessageEvent()) {
                    final TextMessageEvent textMessageEvent = event.asTextMessageEvent();
                    final String messageId = textMessageEvent.messageId();
                    final String text = textMessageEvent.text();

                    FbMessage fbMessage = new FbMessage.NewMessageBuilder(senderId, timestamp)
                            .setMessageId(messageId)
                            .setTextMessage(text)
                            .setRecipientId(recipientId)
                            .build();

                    inboundMessageBusiness.handleTextMessage(fbMessage);

                }
                else if (event.isAttachmentMessageEvent()) {
                    final AttachmentMessageEvent attachmentMessageEvent = event.asAttachmentMessageEvent();
                    for (Attachment attachment : attachmentMessageEvent.attachments()) {
                        if (attachment.isRichMediaAttachment()) {
                            final RichMediaAttachment richMediaAttachment = attachment.asRichMediaAttachment();
                            final RichMediaAttachment.Type type = richMediaAttachment.type();
                            final URL url = richMediaAttachment.url();
                            logger.debug("Received rich media attachment of type '{}' with url: {}", type, url);


                            FbMessage fbMessage = new FbMessage.NewMessageBuilder(senderId, timestamp)
                                    .setRecipientId(recipientId)
                                    .setAttachmentUrl(url)
                                    .setAttachmentType(type.name())
                                    .build();
                            inboundMessageBusiness.handleMediaAttachmentMessage(fbMessage);
                        }
                        if (attachment.isLocationAttachment()) {
                            final LocationAttachment locationAttachment = attachment.asLocationAttachment();
                            final double longitude = locationAttachment.longitude();
                            final double latitude = locationAttachment.latitude();
                            logger.debug("Received location information (long: {}, lat: {})", longitude, latitude);

                            FbMessage fbMessage = new FbMessage.NewMessageBuilder(senderId, timestamp)
                                    .setRecipientId(recipientId)
                                    .setLocation(longitude, latitude)
                                    .build();

                            inboundMessageBusiness.handleLocationAttachmentMessage(fbMessage);
                        }
                    }
                }
            });
            logger.debug("Processed callback payload successfully");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessengerVerificationException e) {
            logger.warn("Processing of callback payload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
