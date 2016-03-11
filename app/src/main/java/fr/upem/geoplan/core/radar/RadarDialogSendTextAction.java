package fr.upem.geoplan.core.radar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.Toast;

import fr.upem.geoplan.R;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.session.User;

public class RadarDialogSendTextAction extends DialogFragment implements DialogInterface.OnClickListener {
    public static final int TEXT_FASTER = 0;
    public static final int TEXT_CHECK = TEXT_FASTER + 1;
    public static final int TEXT_CANCEL = TEXT_CHECK + 1;

    private User user;
    private Event event;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();

        user = bundle.getParcelable("user");
        event = bundle.getParcelable("event");

        assert user != null;
        builder.setTitle(user.getDisplayName()).setItems(R.array.dialog_touch_send_text_action, this);
        builder.setCancelable(true);

        return builder.create();
    }

    private static class SentMessageListener extends BroadcastReceiver {
        private final User user;

        SentMessageListener(User user) {
            this.user = user;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Message sent to " + (user.getFirstname() + user.getLastname()), Toast.LENGTH_SHORT).show();
        }
    }

    private static class DeliveredMessageListener extends BroadcastReceiver {
        private final User user;

        DeliveredMessageListener(User user) {
            this.user = user;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Message delivered to " + (user.getFirstname() + user.getLastname()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String body;

        switch (which) {
            case TEXT_FASTER:
                body = "Please, hurry up !";
                break;
            case TEXT_CHECK:
                body = "Are you OK ?";
                break;
            case TEXT_CANCEL:
                body = "Sorry, " + event.getName() + " is canceled";
                break;
            default:
                throw new RuntimeException("Unknown action");
        }

        final Context context = getActivity();

        final SentMessageListener sentMessageListener = new SentMessageListener(user);
        context.registerReceiver(sentMessageListener, new IntentFilter("SMS_SENT"));

        final DeliveredMessageListener deliveredMessageListener = new DeliveredMessageListener(user);
        context.registerReceiver(deliveredMessageListener, new IntentFilter("SMS_DELIVERED"));

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);

        SmsManager sms = SmsManager.getDefault();
        try {
            sms.sendTextMessage(user.getPhone(), null, body, sentPI, deliveredPI);
        } catch (Exception e) {
            Toast.makeText(context, String.format("Cannot send message\n%s", e.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.unregisterReceiver(sentMessageListener);
                context.unregisterReceiver(deliveredMessageListener);
            }
        }, 10000);
    }
}
