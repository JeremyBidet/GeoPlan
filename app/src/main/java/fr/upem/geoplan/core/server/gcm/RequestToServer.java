package fr.upem.geoplan.core.server.gcm;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

import fr.upem.geoplan.R;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.session.User;

public class RequestToServer {
    private static final String LOG_TAG = "RequestToServer";
    private String USER_ID = null;
    private final Context context;
    private final GoogleCloudMessaging gcm;
    private final String SenderID;

    public RequestToServer(Context context) {
        this.context = context;
        this.gcm =  GoogleCloudMessaging.getInstance(context);
        this.SenderID = String.valueOf(R.string.sender_id);
    }



    private void sendGCMMessage(Bundle dataSend) {
        final Bundle dataTmp = dataSend;

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    Bundle data = dataTmp;
                    String id = Integer.toString(getNextMsgId());
                    gcm.send(SenderID + "@gcm.googleapis.com", id, data);
                    msg = "Sent message";
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }


            @Override
            protected void onPostExecute(String msg) {
                if (msg != null) {
                    Log.w(LOG_TAG, String.format("send message failed (%s)", msg));
                    Toast.makeText(context,
                            "send message failed: " + msg,
                            Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);

    }

    private void receivedGCMMessage() {

    }

    private int getNextMsgId() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int id = prefs.getInt(DataConstantGcm.KEY_MSG_ID, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DataConstantGcm.KEY_MSG_ID, ++id);
        editor.commit();
        return id;
    }

    public User createUser(User user) {
        if (USER_ID == null) {
            TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            USER_ID = tele.getDeviceId();
        }
        Bundle data = new Bundle();
        data.putString("action", DataConstantGcm.ACTION_CREATE_USER);
        data.putString(DataConstantGcm.USER_ID, USER_ID);
        data.putString(DataConstantGcm.FIRST_NAME, user.getFirstname());
        data.putString(DataConstantGcm.LAST_NAME, user.getLastname());
        data.putString(DataConstantGcm.PHONE, user.getPhone());
        data.putString(DataConstantGcm.EMAIL, user.getEmail());
        sendGCMMessage(data);

        return new User(USER_ID, user);
    }

    public void createEvent(Event event) {
        Bundle data = new Bundle();
        //reception je reçois l'id (_id), action="receivedEventId"
        data.putString("action", DataConstantGcm.ACTION_CREATE_EVENT);
        data.putString(DataConstantGcm.EVENT_NAME, event.getName());
        data.putString(DataConstantGcm.EVENT_DESCRIPTION, event.getDescription());
        data.putString(DataConstantGcm.EVENT_LOCALIZATION, event.getLocalization());
        data.putLong(DataConstantGcm.EVENT_START_DATE_TIME, event.getStart_date_time().getTime());
        data.putLong(DataConstantGcm.EVENT_END_DATE_TIME, event.getEnd_date_time().getTime());
        data.putString(DataConstantGcm.EVENT_GUESTS_ID, event.getName());
        data.putInt(DataConstantGcm.EVENT_WEIGHT, event.getWeight());
        data.putString(DataConstantGcm.EVENT_TYPE, event.getType());
        data.putFloat(DataConstantGcm.EVENT_COST, event.getCost());
        data.putInt(DataConstantGcm.EVENT_COLOR, event.getColor());
        sendGCMMessage(data);
    }

    public ArrayList<Event> getAllEventsOwned() {
        TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        USER_ID = tele.getDeviceId();

        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_GET_ALL_EVENTS_OWNED);
        data.putString(DataConstantGcm.USER_ID, USER_ID);
        sendGCMMessage(data);
        return null;

    }

    public ArrayList<Event> getAllEventsGuested() {
        TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        USER_ID = tele.getDeviceId();

        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_GET_ALL_EVENTS_GUESTED);
        data.putString(DataConstantGcm.USER_ID, USER_ID);
        sendGCMMessage(data);
        return null;

    }

    public ArrayList<String> getUsers() {
        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_GET_USERS);
        sendGCMMessage(data);
        return null;
    }

    public void updatePosition(LatLng position, long eventId) {
        TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        USER_ID = tele.getDeviceId();

        Bundle data = new Bundle();
        // en envoie userID, lat, lng, eventID
        // autre méthode on reçoit tous les param user + lat + lng (methode getPosition.
        data.putString("action", DataConstantGcm.ACTION_UPDATE_POSITION);
        data.putString(DataConstantGcm.USER_ID, USER_ID);
        data.putLong(DataConstantGcm.EVENT_ID, eventId);
        data.putDouble(DataConstantGcm.POSITION_LATITUDE, position.latitude);
        data.putDouble(DataConstantGcm.POSITION_LONGITUDE, position.longitude);
        sendGCMMessage(data);
    }

    public void addUserToEvent(User user, Event event) {
        Bundle data = new Bundle();
        String userId = user.getID();

        data.putString("action", DataConstantGcm.ACTION_ADD_USERS_TO_EVENT);
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putString("userId", userId);
        sendGCMMessage(data);
    }

    public void removeUserToEvent(User user, Event event) {
        Bundle data = new Bundle();
        String userId = user.getID();

        data.putString("action", DataConstantGcm.ACTION_REMOVE_USERS_TO_EVENT);
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putString("userId", userId);
        sendGCMMessage(data);
    }

    public void updateEvent(Event event) {
        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_UPDATE_EVENT);
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putString(DataConstantGcm.EVENT_NAME, event.getName());
        data.putString(DataConstantGcm.EVENT_DESCRIPTION, event.getDescription());
        data.putDouble(DataConstantGcm.POSITION_LATITUDE, event.getPosition().latitude);
        data.putDouble(DataConstantGcm.POSITION_LONGITUDE, event.getPosition().longitude);
        data.putString(DataConstantGcm.EVENT_LOCALIZATION, event.getLocalization());
        data.putLong(DataConstantGcm.EVENT_START_DATE_TIME, event.getStart_date_time().getTime());
        data.putLong(DataConstantGcm.EVENT_END_DATE_TIME, event.getEnd_date_time().getTime());
        sendGCMMessage(data);
        // boucle for
        //event.getOwners()
        //event.getGuests()

    }

    public static void updateUser(User user, LatLng position) {
        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_UPDATE_USER);
                data.putDouble(DataConstantGcm.POSITION_LATITUDE, position.latitude);
        data.putDouble(DataConstantGcm.POSITION_LONGITUDE, position.longitude);
    }

    public static LatLng getPosition(User user) throws IllegalAccessException {
        Bundle data = new Bundle();

        return null;
    }



}
