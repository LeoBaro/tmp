package unibo.springSAG.connection.websocket;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import unibo.basicomm23.utils.CommUtils;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandler extends AbstractWebSocketHandler  { //implements WebSocketHandler interface
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private static final String className        = "WebSocketHandler";

    public void invalidateSessions() {
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            WebSocketSession session = iter.next();
            try{
                session.close();
            }catch( Exception e ){
                CommUtils.outred(className + " | invalidateAndReloadSession ERROR:"+e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        CommUtils.outblue(className + " | Added the session:" + session);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        CommUtils.outblue(className + " | Removed the session:" + session);
        super.afterConnectionClosed(session, status);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println(className + " | handleTextMessage Received: " + message);
        String cmd = message.getPayload();
        sendToAll("echo: "+cmd);
    }
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println(className + " | handleBinaryMessage Received " );
        //Send to all the connected clients
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }

    public void sendToAll(String message)  {
        try{
            CommUtils.outblue(className + " | sendToAll:" + message);
            sendToAll( new TextMessage(message)) ;
        }catch( Exception e ){
            CommUtils.outred(className + " | sendToAll ERROR:"+e.getMessage());
        }

    }
    public void sendToAll(TextMessage message) throws IOException{
        Iterator<WebSocketSession> iter = sessions.iterator();
        while( iter.hasNext() ){
            iter.next().sendMessage(message);
        }
    }

}