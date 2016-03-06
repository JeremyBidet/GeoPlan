package fr.upem.firecloud;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.ArrayList;

public class ServerApp {
    private final String path;
    private final Firebase mRootRef;

    public ServerApp(String path) {
        this.path = path;
        this.mRootRef = new Firebase(path);
    }

    public FireCloudUser createUser(long idUser, String firstName, String lastName, LatLng position, String phoneNumber) {
        FireCloudUser user = new FireCloudUser(idUser, firstName, lastName, position, phoneNumber, null);
        Firebase userRef = mRootRef.child("Users").child(firstName.substring(0,1)+lastName);
        userRef.setValue(user);
        return user;
    }

    public FireCloudEvent createEvent(long id, ArrayList<Long> ownersId, String title, String description, ArrayList<Long> guestsId, Date start_date_time, Date end_date_time, String localization, LatLng position) {
        FireCloudEvent event = new FireCloudEvent(id, ownersId, title, description, guestsId, start_date_time, end_date_time, localization, position);
        Firebase eventRef = mRootRef.child("Events").child(title);
        eventRef.setValue(event);
        return event;
    }
    public ArrayList<Long> getAllEvents(long idUser) {
        FireCloudUser user = null;
        ArrayList<Long> eventsUser = user.getEventsUser();
        if (eventsUser == null || eventsUser.isEmpty()) {

        }
        return eventsUser;
    }

    public LatLng getPosition(long idUser) {
        //TODO Code Firebase pour récupérer dans la base le user avec cet id.
        FireCloudUser user = null;
        return user.getPosition();
    }

    public void updatePositionUser(LatLng position, long idUser) {
        //TODO Code Firebase pour récupérer la position dans firebase et le mettre à jour
        FireCloudUser user = null;
        user.setPosition(position);
    }

    public void addUserToEvent() {
        //TODO Code Firebase pour ajouter un utilisateur dans l'event (guests) et un event dans liste event de user (eventsUser)
    }

    public void removeUserToEvent() {
        //TODO Code Firebase pour supprimer un utilisateur dans l'event (guests) et un event dans liste event de user (eventsUser)
    }

    public void updateEvent(long eventId) {
        //TODO Code Firebase pour mettre à jour les paramètre de l'event.
    }
}
