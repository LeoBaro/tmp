package unibo.springSAG.connection.websocket;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.basicomm23.utils.CommUtils;

public class CoapObserver implements CoapHandler {
    private static final String className        = "CoapObserver";
    @Override
    public void onLoad(CoapResponse response) {
        CommUtils.outcyan(className + " | changed: " + response.getResponseText() );
        //send info over the websocket
        try {
            Thread.sleep(300); //Per dare tempo alla pagina di visualizzarsi
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebSocketConfiguration.wshandler.sendToAll("" + response.getResponseText());
    }

    @Override
    public void onError() {
        CommUtils.outred(className + " | ERROR");
    }
}
