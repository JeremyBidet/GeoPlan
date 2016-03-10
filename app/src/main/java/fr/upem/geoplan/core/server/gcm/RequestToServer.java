package fr.upem.geoplan.core.server.gcm;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.upem.geoplan.R;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.session.User;

public class RequestToServer {
    private String USER_ID = null;
    private final Context context;
    private final GoogleCloudMessaging gcm;
    private final String mSenderID = R.string.sender_id;

    private void sendGCMMessage(Bundle dataSend) {
        final Bundle dataTmp = dataSend;
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    Bundle data = dataTmp;
                    //String id = Integer.toString(msgId.incrementAndGet());
                    //gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
                    msg = "Sent message";
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }


            @Override
            protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);

    }

    private void receivedGCMMessage() {

    }

    public User createUser(User user) {
        if (USER_ID == null) {
            TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            USER_ID = tele.getDeviceId();
        }
        Bundle data = new Bundle();
        data.putString(DataConstantGcm.ACTION_CREATEUSER, "createUser");
        data.putString(DataConstantGcm.USER_ID, USER_ID);
        data.putString(DataConstantGcm.FIRST_NAME, user.getFirstname());
        data.putString(DataConstantGcm.LAST_NAME, user.getLastname());
        data.putString(DataConstantGcm.PHONE, user.getPhone());

        sendGCMMessage(data);

        return new User(USER_ID);
    }

    public void createEvent(Event event) {
        Bundle data = new Bundle();

        data.putString(DataConstantGcm.ACTION_CREATEVENT, "createEvent");
    }

    public ArrayList<Event> getAllEvents() {
        TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        USER_ID = tele.getDeviceId();

        Bundle data = new Bundle();

        data.putString(DataConstantGcm.ACTION_GETALLEVENTS, "getAllEvents");
        data.putString(DataConstantGcm.USER_ID, USER_ID);

        return null;

    }

    public ArrayList<Long> getUsers() {
        Bundle data = new Bundle();

        data.putString(DataConstantGcm.ACTION_GETUSERS, "getUsers");

        return null;
    }

    public void updatePosition(LatLng position) {
        Bundle data = new Bundle();

        data.putString(DataConstantGcm.ACTION_UPDATEPOSITION, "updatePosition");
        data.putDouble(DataConstantGcm.POSITION_LATITUDE, position.latitude);
        data.putDouble(DataConstantGcm.POSITION_LONGITUDE, position.longitude);
    }

    public void addUsersToEvent(List<User> users, Event event) {
        Bundle data = new Bundle();
        ArrayList<String> guestsId = new ArrayList<>();

        for(User user:users) {
            guestsId.add(user.getID());
        }
        data.putString(DataConstantGcm.ACTION_ADDUSERSTOEVENT, "addUsersToEvent");
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putStringArrayList(DataConstantGcm.EVENT_GUESTS_ID,guestsId);
    }

    public void removeUsersToEvent(List<User> users, Event event) {
        Bundle data = new Bundle();
        ArrayList<String> guestsId = new ArrayList<>();

        for(User user:users) {
            guestsId.add(user.getID());
        }
        data.putString(DataConstantGcm.ACTION_REMOVEUSERSTOEVENT, "removeUsersToEvent");
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putStringArrayList(DataConstantGcm.EVENT_GUESTS_ID, guestsId);
    }

    public void updateEvent(Event event) {
        Bundle data = new Bundle();

        data.putString(DataConstantGcm.ACTION_UPDATEEVENT, "updateEvent");
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putString(DataConstantGcm.EVENT_NAME, event.getName());
        data.putString(DataConstantGcm.EVENT_DESCRIPTION, event.getDescription());
        //data.putDouble(DataConstantGcm.POSITION_LATITUDE, event.);
        //data.putDouble(DataConstantGcm.POSITION_LONGITUDE, event.);
        data.putString(DataConstantGcm.EVENT_LOCALIZATION, event.getLocalization());
        data.putLong(DataConstantGcm.EVENT_START_DATE_TIME, event.getStart_date_time().getTime());
        data.putLong(DataConstantGcm.EVENT_END_DATE_TIME, event.getEnd_date_time().getTime());

    }

    /* Optional or deprecated method
    public static LatLng getPosition(User user) throws IllegalAccessException {
        return null;
    }


    public static void updateUser(User user) {
    }
    */
}
