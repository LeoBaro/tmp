package unibo.springSAG.connection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommUtils;

@Component
public class SagConnection extends ConnectionUtils {

    private static final String className = "SagConnection";
    
    @Value("${actor.ticketservice.ctx}")
    String actorCtx;
    @Value("${actor.ticketservice.name}")
    String actorName;
    @Value("${actor.ticketservice.ctx.port}")
    String actorCtxPort;
    @Value("${actor.ticketservice.ctx.host}")
    String actorCtxHost;

    @Value("${actor.coldroom.ctx}")
    String actorCooldRoomCtx;
    @Value("${actor.coldroom.name}")
    String actorColdRoomName;
    @Value("${actor.coldroom.ctx.port}")
    String actorColdRoomCtxPort;
    @Value("${actor.coldroom.ctx.host}")
    String actorColdRoomCtxHost;
    

    
    public CoapConnectionHighTimeout connectLocalActorUsingCoap(String actorName) {
        CommUtils.outred(className + "| actorCtx: "+actorCtx+" actorName: "+actorName+ " actorCtxPort: "+actorCtxPort+ "actorCtxHost: "+actorCtxHost);
        return connectActorUsingCoap(actorCtxHost, actorCtxPort, actorCtx, actorName);
    }

    public String askAvailability(Interaction conn) {
        String functionName = "askAvailability";
        try {
            String msg = "" + CommUtils.buildRequest("ServiceAccessGUI","getavailability", "getavailability(arg)", actorName);
            return sendRequest(conn, msg, functionName);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return null;
    }

    public String sendStorageRequest(Interaction conn, float fw) {
        String functionName = "sendStorageRequest";
        try {
            String msg = "" + CommUtils.buildRequest("ServiceAccessGUI", "storerequest", "storerequest(" + fw + ")", actorName);

            return sendRequest(conn, msg, functionName);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return null;
    }

    public String sendChargeStatusRequest(Interaction conn) {
        String functionName = "sendChargeStatusRequest";
        try {
            String msg = "" + CommUtils.buildRequest("ServiceAccessGUI", "getticketstatus", "getticketstatus(arg)", actorName);
            return sendRequest(conn, msg, functionName);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return null;
    }

    public String enterTicketRequest(Interaction conn, String ticketCode) {
        String functionName = "enterTicketRequest";
        try {
            String msg = ""+ CommUtils.buildRequest("ServiceAccessGUI",
                    "atindoor", "atindoor("+ticketCode+")", actorName);
            return sendRequest(conn, msg, functionName);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return null;
    }

    private String sendRequest(Interaction conn, String msg, String functionName) {
        String answer = "";
        try {
            CommUtils.outblue(className + " " + functionName + " | msg:" + msg + ", conn: " + conn);
            answer = conn.request(msg);
            CommUtils.outmagenta(className + " " + functionName + " | answer: " + answer);
        } catch (Exception e) {
            CommUtils.outred(className + " " + functionName + " | ERROR: " + e.getMessage());
        }
        return answer;
    }
}