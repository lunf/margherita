package lunf.queen.margherita.service;

import lunf.queen.margherita.entity.JmMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TextMessageProcessingServiceImpl implements MessageProcessingService {

    private static final Logger logger = LoggerFactory.getLogger(TextMessageProcessingServiceImpl.class);

    @Override
    public void processMessage(JmMessage jmMessage) throws InterruptedException {
        logger.debug(String.format("Text message service started for Sender ID: {%s}" , jmMessage.getSenderId()));

        Thread.sleep(2000);

        logger.debug(String.format("Text message service ended for Sender ID: {%s}", jmMessage.getSenderId()));
    }
}
