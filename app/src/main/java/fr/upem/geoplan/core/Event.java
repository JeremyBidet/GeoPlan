package fr.upem.geoplan.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public abstract class Event implements Parcelable {
    private String name;
    private String description;

    private LatLng position;
    private String localization;

    private Date start_date_time;
    private Date end_date_time;

    private final ArrayList<User> guests = new ArrayList<>();
    private final ArrayList<User> owners = new ArrayList<>();

    public Event(String name, Date start_date_time, Date end_date_time, String localization, LatLng position, Collection<User> guests, Collection<User> owners, String description) {
        this.name = name;
        this.description = description;

        this.localization = localization;
        this.position = position;

        this.start_date_time = start_date_time;
        this.end_date_time = end_date_time;

        this.guests.addAll(guests);
        this.owners.addAll(owners);
    }

    public Event(Parcel in) {
        name = in.readString();
        description = in.readString();

        localization = in.readString();
        Double lat = in.readDouble();
        Double lon = in.readDouble();
        position = new LatLng(lat, lon);

        start_date_time = new Date(in.readLong());
        end_date_time = new Date(in.readLong());

        in.readTypedList(guests, getUserCreator());
        in.readTypedList(owners, getUserCreator());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);

        dest.writeString(localization);
        dest.writeDouble(position.latitude);
        dest.writeDouble(position.longitude);

        dest.writeLong(start_date_time.getTime());
        dest.writeLong(end_date_time.getTime());

        dest.writeTypedList(guests);
        dest.writeTypedList(owners);
    }

    protected Creator<User> getUserCreator() {
        return User.CREATOR;
    }

    public String getName() {
        return name;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getLocalization() {
        return localization;
    }

    public Date getStart_date_time() {
        return new Date(this.start_date_time.getTime());
    }

    public Date getEnd_date_time() {
        return new Date(this.end_date_time.getTime());
    }

    public List<User> getGuests() {
        ArrayList<User> guests = new ArrayList<>();
        guests.addAll(this.guests);
        return guests;
    }

    public ArrayList<User> getOwners() {
        ArrayList<User> owners = new ArrayList<>();
        owners.addAll(this.owners);
        return owners;
    }

    @Override
    public String toString() {
        return name + "(" + position.latitude + ";" + position.longitude + ") - " + guests.size() + " guests";
    }
}
