package fr.upem.firecloud;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jeremie on 01/03/2016.
 */
public class FireCloudUser implements Parcelable{
    private final int idUser;
    private final String firstName;
    private final String lastName;
    private LatLng position;
    private final String phoneNumber;


    public FireCloudUser(int idUser, String firstName, String lastName, LatLng position, String phoneNumber) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.phoneNumber = phoneNumber;
    }

    protected FireCloudUser(Parcel in) {
        idUser = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        Double lat = in.readDouble();
        Double lon = in.readDouble();
        position = new LatLng(lat, lon);
        phoneNumber = in.readString();
    }

    public static final Creator<FireCloudUser> CREATOR = new Creator<FireCloudUser>() {
        @Override
        public FireCloudUser createFromParcel(Parcel in) {
            return new FireCloudUser(in);
        }

        @Override
        public FireCloudUser[] newArray(int size) {
            return new FireCloudUser[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idUser);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeDouble(position.latitude);
        dest.writeDouble(position.longitude);
        dest.writeString(phoneNumber);
    }

    public int getIdUser() { return idUser; }

    public String getfirstName() { return firstName; }

    public String getlastName() { return lastName; }

    public LatLng getEventPosition() { return position; }

    public void setPosition(LatLng position) { this.position = position; }

    public String getPhoneNumber() { return phoneNumber; }
}
