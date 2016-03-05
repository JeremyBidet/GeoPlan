package fr.upem.firecloud;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class FireCloudUser {
    private final long idUser;
    private final String firstName;
    private final String lastName;
    private LatLng position;
    private final String phoneNumber;


    public FireCloudUser(long idUser, String firstName, String lastName, LatLng position, String phoneNumber) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.phoneNumber = phoneNumber;
    }

    public long getIdUser() { return idUser; }

    public String getfirstName() { return firstName; }

    public String getlastName() { return lastName; }

    public LatLng getEventPosition() { return position; }

    public void setPosition(LatLng position) { this.position = position; }

    public String getPhoneNumber() { return phoneNumber; }
}
