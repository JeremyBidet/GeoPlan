package fr.upem.firecloud;

public class ProcessorFactory {

    private static final String PACKAGE = "com.grokkingandroid.sampleapp.samples.gcm";
    private static final String ACTION_REGISTER = PACKAGE + ".REGISTER";
    private static final String ACTION_ECHO = PACKAGE + ".ECHO";
    private static final String ACTION_MESSAGE = PACKAGE + ".MESSAGE";

    public static PayloadProcessor getProcessor(String action) {
        if (action == null) {
            throw new IllegalStateException("action must not be null");
        }
        if (action.equals(ACTION_REGISTER)) {
            return new RegisterProcessor();
        }
        else if (action.equals(ACTION_ECHO)) {
            return new EchoProcessor();
        }
        else if (action.equals(ACTION_MESSAGE)) {
            return new MessageProcessor();
        }
        throw new IllegalStateException("Action " + action + " is unknown");
    }
}

