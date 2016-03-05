package fr.upem.firecloud;

import org.jivesoftware.smack.XMPPException;

import java.util.HashMap;
import java.util.Map;

import static fr.upem.firecloud.CcsClient.createJsonMessage;

public class ServerMain {
    public static void main(String[] args) {
        final String projectId = "geoplan-cd1dd";
        final String apiKey = "AIzaSyDLt1dgAn154JKtFknBgK7eV4RUvTc2GS4";
        final String toRegId = args[2];

        CcsClient ccsClient = new CcsClient(projectId, apiKey, true);

        try {
            ccsClient.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        // Send a sample hello downstream message to a device.
        String messageId = ccsClient.getRandomMessageId();
        Map<String, String> payload = new HashMap<>();
        payload.put("message", "Simple sample sessage");
        String collapseKey = "sample";
        Long timeToLive = 10000L;
        Boolean delayWhileIdle = true;
        ccsClient.send(createJsonMessage(toRegId, messageId, payload, collapseKey,
                timeToLive, delayWhileIdle));
    }
}
