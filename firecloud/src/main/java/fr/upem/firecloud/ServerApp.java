package fr.upem.firecloud;

import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerApp {
    private final static String NODE_USERS= "Users";
    private final static String NODE_EVENTS= "Events";
    private final String path;
    private final Firebase mRootRef;


    public ServerApp(String path) {
        this.path = path;
        this.mRootRef = new Firebase(path);
    }

    public long createUser(long idUser, String device, String firstName, String lastName, LatLng position, String phoneNumber) {
        FireCloudUser user = new FireCloudUser(null,phoneNumber,position,lastName,firstName,device,idUser);
        Firebase userRef = mRootRef.child(NODE_USERS).child(firstName.substring(0, 1) + lastName);
        userRef.setValue(user);
        return user.getUserId();
    }

    public void createUser(long userId, Map<String, Object> userJSON) {
        Firebase userRef = mRootRef.child(NODE_USERS).child(String.valueOf(userId));
        userRef.setValue(userJSON);
    }

    public void createEvent(long eventId, Map<String, Object> eventJSON) {
        Firebase eventRef = mRootRef.child(NODE_EVENTS).child(String.valueOf(eventId));
        eventRef.setValue(eventJSON);
    }

    public long createEvent(long eventId, ArrayList<Long> ownersId, String title, String description, ArrayList<Long> guestsId, long startDateTime, long endDateTime, String localization, LatLng position) {
        FireCloudEvent event = new FireCloudEvent(eventId, ownersId, title, description, guestsId, startDateTime, endDateTime, localization, position);
        Firebase eventRef = mRootRef.child(NODE_EVENTS).child(String.valueOf(eventId));
        eventRef.setValue(event);
        return event.getEventId();
    }

    public ArrayList<Long> getAllEvents(long userId) {
        //TODO Code Firebase pour récupérer dans la base tous les events associés à l'utilisateur.
        FireCloudUser user = null;
        ArrayList<Long> eventsUser = user.getEventsUser();
        return eventsUser;
    }

    public LatLng getPosition(long userID) {
        //TODO Code Firebase pour récupérer dans la base le user avec cet id.
        final Firebase userRef = mRootRef.child(NODE_EVENTS).child(String.valueOf(userID));
        LatLng position = new LatLng(12.5, 45.6);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FireCloudUser user = dataSnapshot.getValue(FireCloudUser.class);
                LatLng p = user.getPosition();
                //user.getPosition()
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("MainActivity", "The read failed: " + firebaseError.getMessage());
            }
        });
        return position;
    }

    public void updatePositionUser(LatLng position, long userId) {
        //TODO Code Firebase pour récupérer la position dans firebase et le mettre à jour
        Map<String, Object> userMap = new HashMap<String, Object>();
        Firebase userRef = mRootRef.child(NODE_USERS).child(String.valueOf(userId));

        if (position != null) { userMap.put("position", position); }
        userRef.updateChildren(userMap);
    }

    public void addUserToEvent(long userId) {
        //TODO Code Firebase pour ajouter un utilisateur dans l'event (guests) et un event dans liste event de user (eventsUser)
        Map<String, Object> userMap = new HashMap<String, Object>();
        List<Long> guestsId = new ArrayList<>();
        Firebase eventGuestsRef = mRootRef.child(NODE_EVENTS).child("guestsId");

        guestsId.add(userId);
        userMap.put("guestsId", guestsId);
        eventGuestsRef.updateChildren(userMap);
    }

    public void addUsersToEvent(List<Long> guestsId) {
        //TODO Code Firebase pour ajouter un utilisateur dans l'event (guests) et un event dans liste event de user (eventsUser)
        Map<String, Object> userMap = new HashMap<String, Object>();
        Firebase eventGuestsRef = mRootRef.child(NODE_EVENTS).child("guestsId");

        userMap.put("guestsId", guestsId);
        eventGuestsRef.updateChildren(userMap);
    }

    public void removeUserToEvent(long userId) {
        //TODO Code Firebase pour supprimer un utilisateur dans l'event (guests) et un event dans liste event de user (eventsUser)
        Firebase eventGuestsRef = mRootRef.child(NODE_EVENTS).child("guestsId").child(String.valueOf(userId));
        eventGuestsRef.removeValue();
    }

    public void removeUsersToEvent(List<Long> guestsId) {
        //TODO Code Firebase pour supprimer un utilisateur dans l'event (guests) et un event dans liste event de user (eventsUser)
        Firebase eventGuestsRef = mRootRef.child(NODE_EVENTS).child("guestsId");
        for(long userId : guestsId) {
            Firebase eventRemoveGuestRef =  eventGuestsRef.child(String.valueOf(userId));
            eventRemoveGuestRef.removeValue();
        }
    }

    public void updateEvent(long eventId, Map<String, Object> eventJSON) {
        Firebase eventRef = mRootRef.child(NODE_EVENTS).child(String.valueOf(eventId));
        eventRef.setValue(eventJSON);
    }

    public void updateEvent(long eventId, ArrayList<Long> ownersId, String title, String description, ArrayList<Long> guestsId, long startDateTime, long endDateTime, String localization, LatLng position) {
        Map<String, Object> eventMap = new HashMap<String, Object>();
        Firebase eventRef = mRootRef.child(NODE_EVENTS).child(String.valueOf(eventId));

        // TODO update ownerId
        if(ownersId != null) { eventMap.put("ownersId", ownersId); }
        if (title != null) { eventMap.put("title", title); }
        if (description != null) { eventMap.put("description", description); }
        if(startDateTime != 0) { eventMap.put("startDateTime", startDateTime); }
        if(endDateTime != 0) { eventMap.put("endDateTime", endDateTime);}
        if(localization != null) { eventMap.put("localization", localization); }
        if(position != null) { eventMap.put("position", position); }
        // TODO update guestsId
        if(guestsId != null) { eventMap.put("guestsId", guestsId); }

        eventRef.updateChildren(eventMap);
    }
}
