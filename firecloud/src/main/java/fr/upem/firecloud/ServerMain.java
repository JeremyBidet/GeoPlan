package fr.upem.firecloud;

import org.jivesoftware.smack.XMPPException;

import java.util.HashMap;
import java.util.Map;

import static fr.upem.firecloud.CcsClient.createJsonMessage;

public class ServerMain {
    public static void main(String[] args) {
        final String projectId = args[0];
        final String password = args[1];
        final String toRegId = args[2];

        CcsClient ccsClient = CcsClient.prepareClient(projectId, password, true);

        try {
            ccsClient.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        // Send a sample hello downstream message to a device.
        String messageId = ccsClient.getRandomMessageId();
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("message", "Simple sample sessage");
        String collapseKey = "sample";
        Long timeToLive = 10000L;
        Boolean delayWhileIdle = true;
        ccsClient.send(createJsonMessage(toRegId, messageId, payload, collapseKey,
                timeToLive, delayWhileIdle));
    }
}
