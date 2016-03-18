package fr.upem.firecloud;

import org.jivesoftware.smack.XMPPException;

public class ServerMain {

    public static void main(String[] args) {
        final long projectId = 900984276030L;
        final String apiKey = args[0];

        CcsClient ccsClient = new CcsClient(projectId, apiKey, true);

        try {
            ccsClient.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
