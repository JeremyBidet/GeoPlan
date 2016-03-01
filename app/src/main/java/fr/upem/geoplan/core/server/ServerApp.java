package fr.upem.geoplan.core.server;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;

import fr.upem.firecloud.FireCloudEvent;
import fr.upem.firecloud.FireCloudUser;

/**
 * Created by Jeremie on 01/03/2016.
 */
public class ServerApp {
    private final String path;
    private final Firebase mRootRef;

    public ServerApp(String path) {
        this.path = path;
        this.mRootRef = new Firebase(path);
    }

    public FireCloudUser createUser(int idUser, String firstName, String lastName, LatLng position, String phoneNumber) {
        FireCloudUser user = new FireCloudUser(idUser, firstName, lastName, position, phoneNumber);
        Firebase userRef = mRootRef.child("Users").child(firstName.substring(0,1)+lastName);
        userRef.setValue(user);
        return user;
    }

    public FireCloudEvent createEvent(int id, String owner, String title, ArrayList<FireCloudUser> guests, DateFormat start_date_time, DateFormat end_date_time, String localization, LatLng position) {
        FireCloudEvent event = new FireCloudEvent(id, owner, title, guests, start_date_time, end_date_time, localization, position);
        Firebase eventRef = mRootRef.child("Events").child(title);
        eventRef.setValue(event);
        return event;
    }

    public LatLng getPosition(FireCloudUser user) {
        return user.getEventPosition();
    }

    public void updatePosition(LatLng position, FireCloudUser user) {
        user.setPosition(position);
    }
}
