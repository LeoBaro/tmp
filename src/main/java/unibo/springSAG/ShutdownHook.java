package unibo.springSAG;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import unibo.springSAG.connection.websocket.WebSocketHandler;

@Component
public class ShutdownHook implements ApplicationListener<ContextClosedEvent> {

    private final WebSocketHandler wshandler;

    @Autowired
    public ShutdownHook(WebSocketHandler wshandler) {
        this.wshandler = wshandler;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        wshandler.sendToAll("stomp");
        wshandler.invalidateSessions();
    }
}
