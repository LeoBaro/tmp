package unibo.springSAG.connection;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.CommSystemConfig;
import unibo.basicomm23.utils.CommUtils;

public class ConnectionUtils {
    private static final String className        = "ConnectionUtils";
    private static Interaction coapconn;
    public static CoapConnectionHighTimeout connectActorUsingCoap(String actorCtxAddr, String actorCtxPort, String actorCtx, String actorName){
        try {
            CommSystemConfig.tracing = true;
            String path   = actorCtx+"/"+actorName;

            coapconn = new CoapConnectionHighTimeout(actorCtxAddr+":"+ actorCtxPort, path );
            CommUtils.outyellow(className + " connectActorUsingCoap | conn:" + coapconn);
        }catch(Exception e){
            CommUtils.outred(className + " connectActorUsingCoap | ERROR: " + e.getMessage());
        }
        return (CoapConnectionHighTimeout) coapconn;
    }

}
