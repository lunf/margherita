package lunf.queen.margherita.consumer;

import lunf.queen.margherita.entity.JmMessage;
import lunf.queen.margherita.service.MessageProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class TextMessageConsumer implements Consumer<Event<JmMessage>> {

    private static final Logger logger = LoggerFactory.getLogger(TextMessageConsumer.class);

    @Autowired
    private MessageProcessingService messageProcessingService;

    @Override
    public void accept(Event<JmMessage> jmMessageEvent) {
        // need to analyse this message
        JmMessage jmMessage = jmMessageEvent.getData();

        try {
            messageProcessingService.processMessage(jmMessage);
        } catch (InterruptedException ex) {
            logger.debug("Processing message {%s} from sender: {%s}", jmMessage.getId(), jmMessage.getSenderId());
        }
    }
}
