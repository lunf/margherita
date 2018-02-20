package lunf.queen.margherita.business;

import lunf.queen.margherita.constants.JmEventConst;
import lunf.queen.margherita.domain.FbMessage;
import lunf.queen.margherita.entity.JmMessage;
import lunf.queen.margherita.repository.JmMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;

@Service
public class InboundMessageBusiness {

    @Autowired
    EventBus eventBus;

    @Autowired
    private JmMessageRepository jmMessageRepository;

    public void handleTextMessage(FbMessage fbMessage){
        JmMessage jmMessage = new JmMessage(JmMessage.Direction.IN_BOUND, JmMessage.Status.RECEIVED);

        jmMessage.setRecipientId(fbMessage.getRecipientId());
        jmMessage.setSenderId(fbMessage.getSenderId());
        jmMessage.setText(fbMessage.getText());

        jmMessageRepository.save(jmMessage);

        eventBus.send(JmEventConst.TEXT_RECEIVED_EVENT, Event.wrap(jmMessage));
    }

    public void handleMediaAttachmentMessage(FbMessage fbMessage) {

    }

    public void handleLocationAttachmentMessage(FbMessage fbMessage) {

    }
}
