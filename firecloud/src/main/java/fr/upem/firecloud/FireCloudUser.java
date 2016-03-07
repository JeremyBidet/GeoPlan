package fr.upem.firecloud;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class FireCloudUser {
    private final long userId;
    private String device;
    private final String firstName;
    private final String lastName;
    private LatLng position;
    private final String phoneNumber;
    private final ArrayList<Long> eventsUser;


    public FireCloudUser(ArrayList<Long> eventsUser, String phoneNumber, LatLng position, String lastName, String firstName, String device, long userId) {
        this.eventsUser = eventsUser;
        this.phoneNumber = phoneNumber;
        this.position = position;
        this.lastName = lastName;
        this.firstName = firstName;
        this.device = device;
        this.userId = userId;
    }

    public long getUserId() { return userId; }

    public String getDevice() { return device; }

    public void setDevice(String device) { this.device = device; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public LatLng getPosition() { return position; }

    public void setPosition(LatLng position) { this.position = position; }

    public String getPhoneNumber() { return phoneNumber; }

    public ArrayList<Long> getEventsUser() { return eventsUser; }
}
