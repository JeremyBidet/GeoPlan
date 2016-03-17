package fr.upem.geoplan.core.session;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

import fr.upem.geoplan.core.planning.Planning;

/**
 * Created by jerem_000 on 03/03/2016.
 */
public class User implements Parcelable {

    private String id;
    private String email = "";
    private String firstname = "";
    private String lastname = "";
    private String phone = "";
    private LatLng position = null;

    public User(String id, User user) {
        this.id = id;
        Parcel parcel = Parcel.obtain();
        user.writeToParcel(parcel, PARCELABLE_WRITE_RETURN_VALUE);
        parcel.setDataPosition(0);
        readFromParcel(parcel);
    }

    public User(String id, String email, String firstname, String lastname, String phone, LatLng position) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.position = position;
    }

    public User() {
        this.id = "";
        this.email = "";
    }

    public Planning syncPlanning() {
        Planning p = new Planning();

        return p;
    }

    protected User(Parcel in) {
        id = readFromParcel(in);
    }

    private String readFromParcel(Parcel in) {
        String id = in.readString();
        email = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        phone = in.readString();
        position = in.readParcelable(LatLng.class.getClassLoader());
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(phone);
        dest.writeParcelable(position, flags);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getDisplayName() {
        StringBuilder displayName = new StringBuilder();

        displayName.append(firstname);

        if (lastname.length() > 0) {
            displayName.append(lastname.substring(0, 1).toUpperCase()).append(".");
        }

        return displayName.toString();
    }

    public String getID() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getPhone() {
        return this.phone;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        assert email != null;

        this.email = email;
    }

    public void setFirstname(String firstname) {
        assert firstname != null;

        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        assert lastname != null;

        this.lastname = lastname;
    }

    public void setPhone(String phone) {
        assert phone != null;

        this.phone = phone;
    }

    public void setPosition(LatLng location) {
        this.position = location;
    }

    @Override
    public String toString() {
        if (email.equals("")) {
            return "Unregistered user";
        }
        return "User " + this.email;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User
                && ((User) o).email.equals(this.email);
    }
}
