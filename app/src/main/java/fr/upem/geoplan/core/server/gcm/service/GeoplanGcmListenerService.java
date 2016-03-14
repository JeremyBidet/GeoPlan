package fr.upem.geoplan.core.server.gcm.service;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import fr.upem.geoplan.R;
import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.session.User;

public class GeoplanGcmListenerService extends GcmListenerService {
    private static final int MESSAGE_NOTIFICATION_ID = 435345;
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
                    synchronized (LockData.lockReceivedEventId) {
                        LockData.receivedEventId = data.getString(DataConstantGcm.EVENT_ID);
                        LockData.doneReceivedEventId = true;
                        LockData.lockReceivedEventId.notify();
                    }
                    break;
                case "updatePosition": // inutile pour les autres teams
                    /*String userId = data.getString(DataConstantGcm.USER_ID);
                    String firstName = data.getString(DataConstantGcm.FIRST_NAME);
                    String lastName = data.getString(DataConstantGcm.LAST_NAME);
                    String phone = data.getString(DataConstantGcm.PHONE);
                    String email = data.getString(DataConstantGcm.EMAIL);*/
                    //besoin que de ça pour server mais inutile pour le reste de l'application.

                    break;
                case "receivedUserPosition":
                    synchronized (LockData.lockReceivedUserPosition) {
                        lat = data.getLong(DataConstantGcm.POSITION_LATITUDE);
                        lng = data.getLong(DataConstantGcm.POSITION_LONGITUDE);
                        //LockData.receivedUserPosition = new LatLng(lat, lng);
                        LockData.doneReceivedUserPosition = true;
                        LockData.lockReceivedUserPosition.notify();
                        sendNotification("Réception position utilisateur");
                    }
                    break;
                case "receivedEventsGuested":
                    //TODO check "s" on event on server
                    synchronized (LockData.lockReceivedEventsGuested) {
                        try {
                            LockData.receivedEventsGuested = parseToGetAllEvent(data);
                            LockData.doneReceivedEventsGuested = true;
                            LockData.lockReceivedEventsGuested.notify();
                            sendNotification("Réception events propriétaire");
                        }catch(JSONException e){
                            Log.e("GCMListenerService", e.getMessage());
                        }
                    }
                    break;
                case "receivedEventsOwned":
                    //TODO check "s" on event on server
                    synchronized (LockData.lockReceivedEventsGuested) {
                        try {
                            LockData.receivedEventsOwned = parseToGetAllEvent(data);
                            LockData.doneReceivedEventsOwned = true;
                            LockData.lockReceivedEventsOwned.notify();
                            sendNotification("Réception events propriétaire");
                        }catch(JSONException e){
                            Log.e("GCMListenerService", e.getMessage());
                        }
                    }
                    break;
                case "receivedUserAccordingToEmail":
                    synchronized (LockData.lockReceivedUserAccordingToMail) {
                        String userId = data.getString(DataConstantGcm.USER_ID);
                        String firstName = data.getString(DataConstantGcm.FIRST_NAME);
                        String lastName = data.getString(DataConstantGcm.LAST_NAME);
                        String phone = data.getString(DataConstantGcm.PHONE);
                        String email = data.getString(DataConstantGcm.EMAIL);
                        LatLng userPosition = new LatLng(data.getLong(DataConstantGcm.POSITION_LATITUDE),
                                data.getLong(DataConstantGcm.POSITION_LONGITUDE));

                        User userTmp = new User(userId, email, firstName, lastName, phone);
                        userTmp.setPosition(userPosition);
                        LockData.receivedUserAccordingToMail = userTmp;
                        LockData.doneReceivedUserAccordingToMail =  true;
                        LockData.lockReceivedUserAccordingToMail.notify();
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

                listEvent.add(new Event(eventId, eventName, description, eventPosition, localization,
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

            User userTmp = new User(userId, email, firstName, lastName, phone);
            userTmp.setPosition(userPosition);
            listUsers.add(userTmp);

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

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("GeoPlan-GCM")
                .setContentText(message);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

}
