package fr.upem.firecloud;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class FireCloudUser {
    private final long userId;
    private final String firstName;
    private final String lastName;
    private LatLng position;
    private final String phoneNumber;
    private final ArrayList<Long> eventsUser;

    public FireCloudUser(long userId, String firstName, String lastName, LatLng position, String phoneNumber, ArrayList<Long> eventsUser) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.eventsUser = eventsUser;
    }

    public long getUserId() { return userId; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public LatLng getPosition() { return position; }

    public void setPosition(LatLng position) { this.position = position; }

    public String getPhoneNumber() { return phoneNumber; }

    public ArrayList<Long> getEventsUser() { return eventsUser; }
}
