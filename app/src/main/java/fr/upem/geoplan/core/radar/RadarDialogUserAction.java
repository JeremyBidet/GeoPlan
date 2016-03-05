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
import fr.upem.geoplan.core.session.User;

public class RadarDialogUserAction extends DialogFragment implements DialogInterface.OnClickListener {
    public static final int CALL_ACTION = 0;
    public static final int MESSAGE_ACTION = CALL_ACTION + 1;
    public static final int REMOVE_ACTION = MESSAGE_ACTION + 1;

    private User user;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();

        user = bundle.getParcelable("user");

        assert user != null;
        builder.setTitle(user.getFirstname() + user.getLastname()).setItems(R.array.dialog_touch_user_action, this);
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
        switch (which) {
            case CALL_ACTION:
                call();
                break;
            case MESSAGE_ACTION:
                sendMessage(getActivity(), "Winter is coming");
                break;
            case REMOVE_ACTION:
                // TODO Remove user from event
                break;
            default:
                throw new RuntimeException("Unknown action");
        }

        System.out.println(which);
    }

    private void call() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + user.getPhone()));
        startActivity(callIntent);
    }

    private void sendMessage(final Context context, String body) {
        final SentMessageListener sentMessageListener = new SentMessageListener(user);
        context.registerReceiver(sentMessageListener, new IntentFilter("SMS_SENT"));

        final DeliveredMessageListener deliveredMessageListener = new DeliveredMessageListener(user);
        context.registerReceiver(deliveredMessageListener, new IntentFilter("SMS_DELIVERED"));

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(user.getPhone(), null, body, sentPI, deliveredPI);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context.unregisterReceiver(sentMessageListener);
                context.unregisterReceiver(deliveredMessageListener);
            }
        }, 10000);
    }
}
