package fr.upem.geoplan.core.server.gcm;


import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.session.User;

public class RequestToServer {
    private static String USER_ID = null;

    public static User createUser(Context context) {
        if (USER_ID == null) {
            TelephonyManager tele = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            USER_ID = tele.getDeviceId();
        }

        return new User(USER_ID);
    }

    public static void createEvent(Event event) {
    }

    public static ArrayList<Event> getAllEvents() {
        //userId
        return null;
    }

    public static LatLng getPosition(User user) throws IllegalAccessException {
        return null;
    }

    public static void updatePosition(LatLng position) {
    }

    public static void addUsersToEvent(List<User> users, Event event) {
    }

    public static void removeUsersToEvent(List<User> users, Event event) {

    }

    public static void updateEvent(Event event) {
    }

    /*
    public static void updateUser(User user) {
    }
    */
}
