package fr.upem.firecloud;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerApp {

    public void createUser(long userId, Map<String, Object> userJSON) {
    }

    public void createEvent(long eventId, Map<String, Object> eventJSON) {
    }

    public ArrayList<Long> getAllEvents(long userId) {
        return null;
    }

    public LatLng getPosition(long userID) {
        return null;
    }

    public void updatePositionUser(LatLng position, long userId) {
    }

    public void addUserToEvent(long userId, long eventId) {
    }

    public void addUsersToEvent(List<Long> guestsId, long eventId) {
    }

    public void removeUserToEvent(long userId, long eventId) {
    }

    public void removeUsersToEvent(List<Long> guestsId, long eventId) {

    }

    public void updateEvent(long eventId, Map<String, Object> eventJSON) {
    }

    public void updateEvent(long eventId, ArrayList<Long> ownersId, String title, String description, ArrayList<Long> guestsId, long startDateTime, long endDateTime, String localization, LatLng position) {
    }
}
