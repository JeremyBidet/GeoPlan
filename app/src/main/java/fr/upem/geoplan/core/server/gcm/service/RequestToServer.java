package fr.upem.geoplan.core.server.gcm.service;


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
import java.util.LinkedList;
import java.util.List;

import fr.upem.geoplan.R;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.server.gcm.DataConstantGcm;
import fr.upem.geoplan.core.session.User;

public class RequestToServer {
    private static final String LOG_TAG = "RequestToServer";
    private static String USER_ID = null;

    private final Context context;
    private final GoogleCloudMessaging gcm;
    private final String SenderID;

    /**
     * Create an instance RequestToServer to initialize GCM.
     *
     * @param context Context of application.
     */
    public RequestToServer(Context context) {
        this.context = context;
        this.gcm = GoogleCloudMessaging.getInstance(context);
        this.SenderID = context.getString(R.string.sender_id);

    }

    /**
     * Send a Data bundle to AppServer (FirecCloud) through GCM.
     *
     * @param dataSend Data bundle containing message data as key/value pairs to send at GCM.
     */
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

    private Object extractObjectFromDataLock(final String fieldName){
        final List<Object> oneElementList = new LinkedList<>();

        new AsyncTask<Void, Void, Object>() {

            @Override
            protected Object doInBackground(Void... params) {
                String capitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                String lockField = "lock" + capitalized;
                String booleanField = "done" + capitalized;
                Object extracted;
                try {
                    synchronized (LockData.class.getDeclaredField(lockField).get(null)){
                        while(!((boolean)LockData.class.getDeclaredField(booleanField).get(null))){
                            LockData.class.getDeclaredField(lockField).get(null).wait();
                        }
                        LockData.class.getDeclaredField(booleanField).set(null, false);
                        extracted = LockData.class.getDeclaredField(fieldName).get(null);
                    }
                } catch (NoSuchFieldException e) {
                    Log.e(LOG_TAG, "Error in LockData, some fields are missing");
                    return null;
                } catch (IllegalAccessException e) {
                    Log.e(LOG_TAG, "Error in LockData, can't access some fields");
                    return null;
                } catch (InterruptedException e) {
                    return null;
                }
                return extracted;
            }

            @Override
            protected void onPostExecute(Object o) {
                oneElementList.add(o);
            }
        }.execute();
        return oneElementList.get(0);
    }

    /**
     * Create a message ID which is incremented from the last ID.
     *
     * @return A message ID.
     */
    private int getNextMsgId() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int id = prefs.getInt(DataConstantGcm.KEY_MSG_ID, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DataConstantGcm.KEY_MSG_ID, ++id);
        editor.commit();
        return id;
    }

    /**
     * Create an user in AppServer.
     *
     * @param user An User object containing all parameters to send AppServer.
     * @return A user created.
     */
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

    /**
     * Create an event in AppServer.
     *
     * @param event An Event object containing all parameters to send AppServer.
     * @return An evenID from event created.
     */
    public String createEvent(Event event) throws InterruptedException {
        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_CREATE_EVENT);
        data.putString(DataConstantGcm.EVENT_NAME, event.getName());
        data.putString(DataConstantGcm.EVENT_DESCRIPTION, event.getDescription());
        data.putString(DataConstantGcm.EVENT_LOCALIZATION, event.getLocalization());
        data.putDouble(DataConstantGcm.POSITION_LATITUDE, event.getPosition().latitude);
        data.putDouble(DataConstantGcm.POSITION_LONGITUDE, event.getPosition().longitude);
        data.putLong(DataConstantGcm.EVENT_START_DATE_TIME, event.getStart_date_time().getTime());
        data.putLong(DataConstantGcm.EVENT_END_DATE_TIME, event.getEnd_date_time().getTime());
        data.putString(DataConstantGcm.EVENT_GUESTS_ID, event.getName());
        data.putInt(DataConstantGcm.EVENT_WEIGHT, event.getWeight());
        data.putString(DataConstantGcm.EVENT_TYPE, event.getType());
        data.putFloat(DataConstantGcm.EVENT_COST, event.getCost());
        data.putInt(DataConstantGcm.EVENT_COLOR, event.getColor());
        sendGCMMessage(data);
        String get;
        synchronized (LockData.lockReceivedEventId){
            while(!LockData.doneReceivedEventId){
                LockData.lockReceivedEventId.wait();
            }
            LockData.doneReceivedEventId = false;
            get = LockData.receivedEventId;
        }
        return get;
    }

    /**
     * Search and get all events associated to owner.
     *
     * @param ownerId An owner identifiant.
     * @return An ArrayList containing all events associated to owner.
     */
    public ArrayList<Event> getAllEventsOwned(String ownerId) {
        /*TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        USER_ID = tele.getDeviceId();*/


        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_GET_ALL_EVENTS_OWNED);
        data.putString(DataConstantGcm.USER_ID, ownerId);
        sendGCMMessage(data);

        // Necessite peut-être attente serveur donc un wait avec une méthode à appeler ici.
        return null;

    }

    /**
     * Search and get all events associated to guest.
     *
     * @param guestId A guest identifiant.
     * @return An ArrayList containing all events associated to guest.
     */
    public ArrayList<Event> getAllEventsGuested(String guestId) {
        /*TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        USER_ID = tele.getDeviceId();*/

        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_GET_ALL_EVENTS_GUESTED);
        data.putString(DataConstantGcm.USER_ID, guestId);
        sendGCMMessage(data);

        // Necessite peut-être attente serveur donc un wait avec une méthode à appeler ici.

        return null;
    }

    /**
     * Get all users registered in our application.
     *
     * @return An ArrayList containing all Users registered in AppServer (Database).
     */
    /*public ArrayList<User> getUsers() {
        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_GET_USERS);
        sendGCMMessage(data);

        // Necessite peut-être attente serveur donc un wait avec une méthode à appeler ici.
        return null;
    }*/

    /**
     * Update position of user.
     *
     * @param position Position GPS to update.
     * @param eventId  Event ID.
     */
    public void updatePosition(LatLng position, long eventId) {
        TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        USER_ID = tele.getDeviceId();

        Bundle data = new Bundle();
        // autre méthode on reçoit tous les param user + lat + lng (methode getPosition.
        data.putString("action", DataConstantGcm.ACTION_UPDATE_POSITION);
        data.putString(DataConstantGcm.USER_ID, USER_ID);
        data.putLong(DataConstantGcm.EVENT_ID, eventId);
        data.putDouble(DataConstantGcm.POSITION_LATITUDE, position.latitude);
        data.putDouble(DataConstantGcm.POSITION_LONGITUDE, position.longitude);
        sendGCMMessage(data);
    }

    /**
     * @param user
     * @return
     * @throws IllegalAccessException
     */
    public LatLng getPosition(User user) throws IllegalAccessException {
        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_GET_POSITION_USER);
        data.putString(DataConstantGcm.USER_ID, user.getID());
        sendGCMMessage(data);


        // Necessite peut-être attente serveur donc un wait avec une méthode à appeler ici.
        return null;
    }

    /**
     * Add an user in Event.
     *
     * @param user  User to add.
     * @param event Specific event to add the user.
     */
    public void addUserToEvent(User user, Event event) {
        Bundle data = new Bundle();
        String userId = user.getID();

        data.putString("action", DataConstantGcm.ACTION_ADD_USERS_TO_EVENT);
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putString("userId", userId);
        sendGCMMessage(data);
    }

    /**
     * Remove an user in Event.
     *
     * @param user  User to remove.
     * @param event Specific event to remove the user.
     */
    public void removeUserToEvent(User user, Event event) {
        Bundle data = new Bundle();
        String userId = user.getID();

        data.putString("action", DataConstantGcm.ACTION_REMOVE_USERS_TO_EVENT);
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putString("userId", userId);
        sendGCMMessage(data);
    }

    /**
     * Update event's parameters.
     *
     * @param event An Event object containing parameters to update.
     */
    public void updateEvent(Event event) {
        Bundle data = new Bundle();
        ArrayList<String> guestsId = new ArrayList<>();
        ArrayList<String> ownersId = new ArrayList<>();

        data.putString("action", DataConstantGcm.ACTION_UPDATE_EVENT);
        data.putLong(DataConstantGcm.EVENT_ID, event.getId());
        data.putString(DataConstantGcm.EVENT_NAME, event.getName());
        data.putString(DataConstantGcm.EVENT_DESCRIPTION, event.getDescription());
        data.putDouble(DataConstantGcm.POSITION_LATITUDE, event.getPosition().latitude);
        data.putDouble(DataConstantGcm.POSITION_LONGITUDE, event.getPosition().longitude);
        data.putString(DataConstantGcm.EVENT_LOCALIZATION, event.getLocalization());
        data.putLong(DataConstantGcm.EVENT_START_DATE_TIME, event.getStart_date_time().getTime());
        data.putLong(DataConstantGcm.EVENT_END_DATE_TIME, event.getEnd_date_time().getTime());

        for (User owner : event.getGuests()) {
            ownersId.add(owner.getID());
        }
        for (User guest : event.getGuests()) {
            guestsId.add(guest.getID());
        }
        data.putStringArrayList(DataConstantGcm.EVENT_GUESTS_ID, guestsId);
        data.putStringArrayList(DataConstantGcm.EVENT_OWNERS_ID, ownersId);
        sendGCMMessage(data);
    }

    /**
     * Update user's parameters.
     *
     * @param user  An User object containing parameters to update.
     */
    public void updateUser(User user) {
        Bundle data = new Bundle();

        data.putString("action", DataConstantGcm.ACTION_UPDATE_USER);
        data.putString(DataConstantGcm.USER_ID, user.getID());
        data.putDouble(DataConstantGcm.POSITION_LATITUDE, user.getPosition().latitude);
        data.putDouble(DataConstantGcm.POSITION_LONGITUDE, user.getPosition().longitude);
        sendGCMMessage(data);
    }
}
