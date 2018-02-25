package lunf.queen.margherita.config;

import lunf.queen.margherita.constants.JmEventConst;
import lunf.queen.margherita.entity.JmMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.bus.Event;
import reactor.bus.EventBus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactorConfigTest {

    @Autowired
    private EventBus eventBus;

    @Test
    public void sendTextMessageEvent() {

        JmMessage jmMessage = new JmMessage(JmMessage.Direction.IN_BOUND, JmMessage.Status.RECEIVED);

        jmMessage.setRecipientId("123456");
        jmMessage.setSenderId("1234567");
        jmMessage.setText("Hello world");

        eventBus.notify(JmEventConst.TEXT_RECEIVED_EVENT, Event.wrap(jmMessage));
    }
}
