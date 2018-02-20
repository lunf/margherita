package lunf.queen.margherita.business;

import lunf.queen.margherita.entity.JmMessage;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;

@Service
public class TextMessageProcessingBusiness implements Consumer<Event<JmMessage>> {

    @Override
    public void accept(Event<JmMessage> jmMessageEvent) {
        // need to analyse this message
    }
}
