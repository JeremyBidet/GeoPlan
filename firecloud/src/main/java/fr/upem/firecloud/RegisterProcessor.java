package fr.upem.firecloud;

/**
 * Handles a user registration.
 */
public class RegisterProcessor implements PayloadProcessor{

    @Override
    public void handleMessage(CcsMessage msg) {
        String accountName = msg.getPayload().get("account");
        PseudoDao.getInstance().addRegistration(msg.getFrom(), accountName);
    }

}