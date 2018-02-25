package lunf.queen.margherita.service;

import lunf.queen.margherita.entity.JmMessage;

public interface MessageProcessingService {
    void processMessage(JmMessage jmMessage) throws InterruptedException;
}
