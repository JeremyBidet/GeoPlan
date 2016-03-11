package fr.upem.geoplan.core.server.gcm.service;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.server.gcm.DataConstantGcm;
import fr.upem.geoplan.core.session.User;

public class GeoplanGcmListenerService extends GcmListenerService {
    private static final String TAG = "GeoPlan";
    private LatLng positionUser;
    private ArrayList<Event> guestEvents;
    private ArrayList<Event> ownerEvents;
    private ArrayList<User> usersRegistered;

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
        // Variable for user
        String userId;
        String email, firstName, lastName, phone;
        LatLng userPosition;

        // Variable for Event
        long eventId;
        String eventName;
        String description;
        LatLng eventPosition;
        String localization;
        Date startDateTime;
        Date endDateTime;
        ArrayList<User> guests = new ArrayList<>();
        ArrayList<User> owners = new ArrayList<>();
        String type;
        int weight;
        float cost;
        int color;

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // Parsing methods received from server

            String action = data.getString(DataConstantGcm.ACTION);
            switch (action) {
                case "receivedEventID":
                    eventId = Long.getLong(data.getString(DataConstantGcm.EVENT_ID));
                    break;
                case "updatePosition":
                    /*String userId = data.getString(DataConstantGcm.USER_ID);
                    String firstName = data.getString(DataConstantGcm.FIRST_NAME);
                    String lastName = data.getString(DataConstantGcm.LAST_NAME);
                    String phone = data.getString(DataConstantGcm.PHONE);
                    String email = data.getString(DataConstantGcm.EMAIL);*/
                    //besoin que de ça pour server mais inutile pour le reste de l'application.
                    lat = data.getLong(DataConstantGcm.POSITION_LATITUDE);
                    lng = data.getLong(DataConstantGcm.POSITION_LONGITUDE);
                    positionUser = new LatLng(lat, lng);
                    break;
                case "receivedUserPosition":
                    lat = data.getLong(DataConstantGcm.POSITION_LATITUDE);
                    lng = data.getLong(DataConstantGcm.POSITION_LONGITUDE);
                    positionUser = new LatLng(lat, lng);
                    break;
                case "receivedEventGuested":
                    guestEvents = new ArrayList<>();

                    //data
                    eventId = Long.parseLong(data.getString(DataConstantGcm.EVENT_ID));
                    eventName = data.getString(DataConstantGcm.EVENT_NAME);
                    description = data.getString(DataConstantGcm.EVENT_DESCRIPTION);
                    localization = data.getString(DataConstantGcm.EVENT_LOCALIZATION);
                    lat = data.getDouble(DataConstantGcm.POSITION_LATITUDE);
                    lng = data.getDouble(DataConstantGcm.POSITION_LONGITUDE);
                    eventPosition = new LatLng(lat, lng);
                    startDateTime = new Date(data.getLong(DataConstantGcm.EVENT_START_DATE_TIME));
                    endDateTime = new Date(data.getLong(DataConstantGcm.EVENT_END_DATE_TIME));
                    //data
                    //data
                    weight = data.getInt(DataConstantGcm.EVENT_WEIGHT);
                    type = data.getString(DataConstantGcm.EVENT_TYPE);
                    cost = data.getFloat(DataConstantGcm.EVENT_COST);
                    color = data.getInt(DataConstantGcm.EVENT_COLOR);
                    break;
                case "receivedEventOwned":
                    ownerEvents = new ArrayList<>();

                    //data
                    eventId = Long.parseLong(data.getString(DataConstantGcm.EVENT_ID));
                    eventName = data.getString(DataConstantGcm.EVENT_NAME);
                    description = data.getString(DataConstantGcm.EVENT_DESCRIPTION);
                    localization = data.getString(DataConstantGcm.EVENT_LOCALIZATION);
                    lat = data.getDouble(DataConstantGcm.POSITION_LATITUDE);
                    lng = data.getDouble(DataConstantGcm.POSITION_LONGITUDE);
                    eventPosition = new LatLng(lat, lng);
                    startDateTime = new Date(data.getLong(DataConstantGcm.EVENT_START_DATE_TIME));
                    endDateTime = new Date(data.getLong(DataConstantGcm.EVENT_END_DATE_TIME));
                    //data
                    //data
                    weight = data.getInt(DataConstantGcm.EVENT_WEIGHT);
                    type = data.getString(DataConstantGcm.EVENT_TYPE);
                    cost = data.getFloat(DataConstantGcm.EVENT_COST);
                    color = data.getInt(DataConstantGcm.EVENT_COLOR);;
                    break;
                case "receivedUsers":
                    usersRegistered = new ArrayList<>();

                    Bundle dataUsers = data.getBundle("users");
                    userId = data.getString(DataConstantGcm.USER_ID);
                    firstName = data.getString(DataConstantGcm.FIRST_NAME);
                    lastName = data.getString(DataConstantGcm.LAST_NAME);
                    phone = data.getString(DataConstantGcm.PHONE);
                    email = data.getString(DataConstantGcm.EMAIL);
                    lat = data.getLong(DataConstantGcm.POSITION_LATITUDE);
                    lng = data.getLong(DataConstantGcm.POSITION_LONGITUDE);

                    break;
            }
            // message received from some topic.
        } else {
            // normal downstream message.

        }
    }
}
