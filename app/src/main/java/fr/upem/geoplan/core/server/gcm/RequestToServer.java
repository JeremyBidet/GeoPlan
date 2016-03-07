package fr.upem.geoplan.core.server.gcm;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestToServer {
    public static void createUser(long userId, Map<String, Object> userJSON) {
    }

    public static void createEvent(long eventId, Map<String, Object> eventJSON) {
    }

    public static ArrayList<Long> getAllEvents(long userId) {
        return null;
    }

    public static LatLng getPosition(long userID) {
        return null;
    }

    public static void updatePositionUser(LatLng position, long userId) {
    }

    public static void addUserToEvent(long userId, long eventId) {
    }

    public static void addUsersToEvent(List<Long> guestsId, long eventId) {
    }

    public static void removeUserToEvent(long userId, long eventId) {
    }

    public static void removeUsersToEvent(List<Long> guestsId, long eventId) {

    }

    public static void updateEvent(long eventId, Map<String, Object> eventJSON) {
    }

    public static void updateEvent(long eventId, ArrayList<Long> ownersId, String title, String description, ArrayList<Long> guestsId, long startDateTime, long endDateTime, String localization, LatLng position) {
    }
}
