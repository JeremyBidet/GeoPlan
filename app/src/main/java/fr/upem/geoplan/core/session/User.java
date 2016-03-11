package fr.upem.geoplan.core.session;

import android.os.Parcel;
import android.os.Parcelable;

import fr.upem.geoplan.core.planning.Planning;

/**
 * Created by jerem_000 on 03/03/2016.
 */
public class User implements Parcelable {
    private final String id;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;

    public User(String id) {
        this.id = id;
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

    ;

    public Planning syncPlanning() {
        Planning p = new Planning();

        return p;
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.email = in.readString();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.phone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.email);
        dest.writeString(this.firstname);
        dest.writeString(this.lastname);
        dest.writeString(this.phone);
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
        return firstname + " " + lastname.substring(0, 1).toUpperCase() + ".";
    }
}
