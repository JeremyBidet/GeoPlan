package fr.upem.geoplan.core.server.gcm.service;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Map;

import fr.upem.geoplan.core.planning.Event;
import fr.upem.geoplan.core.session.User;

class LockData {
    static String receivedEventId;
    static boolean doneReceivedEventId;
    final static Object lockReceivedEventId = new Object();

    static User receivedUserPosition;
    static boolean doneReceivedUserPosition;
    final static Object lockReceivedUserPosition = new Object();

    static List<Event> receivedEventsGuested;
    static boolean doneReceivedEventsGuested;
    final static Object lockReceivedEventsGuested = new Object();

    static List<Event> receivedEventsOwned;
    static boolean doneReceivedEventsOwned;
    final static Object lockReceivedEventsOwned = new Object();

    static User receivedUserAccordingToMail;
    static boolean doneReceivedUserAccordingToMail;
    final static Object lockReceivedUserAccordingToMail = new Object();
}
