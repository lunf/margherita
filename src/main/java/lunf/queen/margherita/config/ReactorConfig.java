package lunf.queen.margherita.config;

import lunf.queen.margherita.constants.JmEventConst;
import lunf.queen.margherita.consumer.TextMessageConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;

import javax.annotation.PostConstruct;

@Configuration
public class ReactorConfig {

    @Autowired
    private EventBus eventBus;

    @Autowired
    private TextMessageConsumer textMessageConsumer;

    @PostConstruct
    public void onStartUp() {
        eventBus.on(Selectors.R(JmEventConst.TEXT_RECEIVED_EVENT), textMessageConsumer);
    }
}
