package fr.upem.geoplan.core.radar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
        builder.setTitle(user.getDisplayName()).setItems(R.array.dialog_touch_user_action, this);
        builder.setCancelable(true);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case CALL_ACTION:
                call();
                break;
            case MESSAGE_ACTION:
                sendMessage();
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

    private void sendMessage() {
        RadarDialogSendTextAction dialog = new RadarDialogSendTextAction();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "SendText");
    }
}
