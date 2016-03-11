package fr.upem.geoplan.core.server.gcm.service;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

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
                    try {
                        guestEvents = parseToGetAllEvent(data);
                    } catch (JSONException e) {
                        Log.e("GCMListenerService", e.getMessage());
                    }
                    break;
                case "receivedEventOwned":
                    try {
                        ownerEvents = parseToGetAllEvent(data);
                    } catch (JSONException e) {
                        Log.e("GCMListenerService", e.getMessage());
                    }
                    break;
                case "receivedUsers":
                    try {
                        JSONObject json = bundleToJsonObject(data);
                        usersRegistered = parserToGetUser(json.getJSONArray("users"));
                    } catch (JSONException e) {
                        Log.e("GCMListenerService", e.getMessage());
                    }
                    break;
            }
            // message received from some topic.
        } else {
            // normal downstream message.

        }
    }

    /**
     * Parse to get attributes of Event and create an array of event.
     *
     * @param data An bundle data to parse containing parameters of event.
     * @return An arrayList containing all User with attribute.
     */
    private ArrayList<Event> parseToGetAllEvent(Bundle data) throws JSONException {
        ArrayList<Event> listEvent = new ArrayList<>();

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

        JSONObject json = bundleToJsonObject(data);
        JSONArray eventJsonArray = json.getJSONArray("events");
        for (int index = 0; index < eventJsonArray.length(); index++) {
            try {
                JSONObject eventJsonObject = eventJsonArray.getJSONObject(index);
                eventId = Long.parseLong(eventJsonObject.getString(DataConstantGcm.EVENT_ID));
                eventName = eventJsonObject.getString(DataConstantGcm.EVENT_NAME);
                description = eventJsonObject.getString(DataConstantGcm.EVENT_DESCRIPTION);
                localization = eventJsonObject.getString(DataConstantGcm.EVENT_LOCALIZATION);
                eventPosition = new LatLng(eventJsonObject.getDouble(DataConstantGcm.POSITION_LATITUDE), eventJsonObject.getDouble(DataConstantGcm.POSITION_LONGITUDE));
                startDateTime = new Date(eventJsonObject.getLong(DataConstantGcm.EVENT_START_DATE_TIME));
                endDateTime = new Date(eventJsonObject.getLong(DataConstantGcm.EVENT_END_DATE_TIME));
                JSONArray ownersJsonArray = eventJsonObject.getJSONArray("owners");
                JSONArray guestsJsonArray = eventJsonObject.getJSONArray("guests");
                owners = parserToGetUser(ownersJsonArray);
                guests = parserToGetUser(guestsJsonArray);

                weight = eventJsonObject.getInt(DataConstantGcm.EVENT_WEIGHT);
                type = eventJsonObject.getString(DataConstantGcm.EVENT_TYPE);

                cost = new Float(eventJsonObject.getDouble(DataConstantGcm.EVENT_COST));
                color = eventJsonObject.getInt(DataConstantGcm.EVENT_COLOR);

                guestEvents.add(new Event(eventId, eventName, description, eventPosition, localization,
                        startDateTime, endDateTime, guests, owners, weight, type, cost, color));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listEvent;
    }

    /**
     * Parse to get attributes of User and create an array of user.
     *
     * @param dataJson An JSON array to parse.
     * @return An arrayList containing all User with attribute.
     */
    private ArrayList<User> parserToGetUser(JSONArray dataJson) throws JSONException {
        ArrayList<User> listUsers = new ArrayList<>();
        // Variable for user
        String userId;
        String email, firstName, lastName, phone;
        LatLng userPosition;

        for(int index = 0; index < dataJson.length(); index++) {
            JSONObject userJsonObj = dataJson.getJSONObject(index);
            userId = userJsonObj.getString(DataConstantGcm.USER_ID);
            firstName = userJsonObj.getString(DataConstantGcm.FIRST_NAME);
            lastName = userJsonObj.getString(DataConstantGcm.LAST_NAME);
            phone = userJsonObj.getString(DataConstantGcm.PHONE);
            email = userJsonObj.getString(DataConstantGcm.EMAIL);
            userPosition = new LatLng(userJsonObj.getLong(DataConstantGcm.POSITION_LATITUDE), userJsonObj.getLong(DataConstantGcm.POSITION_LONGITUDE));

            listUsers.add(new User(userId, email, firstName, lastName, phone));
        }

        return listUsers;
    }

    /**
     * Convert a bundle to JSON Object.
     * @param data A bundle data containing all informations to parse.
     * @return a JSON object.
     */
    private JSONObject bundleToJsonObject(Bundle data) {
        JSONObject json = new JSONObject();
        Set<String> keys = data.keySet();
        for (String key : keys) {
            try {
                json.put(key, data.get(key));
            } catch (JSONException e) {
                Log.e("GCMListenerService", e.getMessage());
            }
        }
        return json;
    }
}
