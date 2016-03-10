package fr.upem.geoplan.core.server.gcm.service;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.ArrayList;

import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.server.gcm.DataConstantGcm;
import fr.upem.geoplan.core.session.User;

public class GeoplanGcmListenerService extends GcmListenerService {
    private static final String TAG = "GeoPlan";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        double lat, lng;
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // Parsing methods received from server

            String action = data.getString(DataConstantGcm.ACTION);
            switch (action) {
                case "receivedEventID":
                    long eventId = Long.getLong(data.getString(DataConstantGcm.EVENT_ID));
                    break;
                case "updatePosition":
                    /*String userId = data.getString(DataConstantGcm.USER_ID);
                    String firstName = data.getString(DataConstantGcm.FIRST_NAME);
                    String lastName = data.getString(DataConstantGcm.LAST_NAME);
                    String phone = data.getString(DataConstantGcm.PHONE);
                    String email = data.getString(DataConstantGcm.EMAIL);*/
                    // besoin que de ça
                    lat = data.getLong(DataConstantGcm.POSITION_LATITUDE);
                    lng = data.getLong(DataConstantGcm.POSITION_LONGITUDE);
                    break;
                case "userPosition":
                    lat = data.getLong(DataConstantGcm.POSITION_LATITUDE);
                    lng = data.getLong(DataConstantGcm.POSITION_LONGITUDE);
                    break;
                case "receivedEventGuested":
                    ArrayList<Event> guestEvent = new ArrayList<>();
                    ArrayList<String> guestEventId;
                    break;
                case "receivedEventOwned":
                    ArrayList<Event> ownerEvent = new ArrayList<>();
                    ArrayList<String> ownerEventId = data.getStringArrayList("");
                    break;
                case "receivedUsers":
                    ArrayList<User> users = new ArrayList<>();
                    break;
            }
            // message received from some topic.
        } else {
            // normal downstream message.

        }
    }
}
