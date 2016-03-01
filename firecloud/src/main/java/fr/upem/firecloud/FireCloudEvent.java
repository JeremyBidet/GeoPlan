package fr.upem.firecloud;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by Jeremie on 01/03/2016.
 */
public class FireCloudEvent implements Parcelable{
    private final int id;
    private final String owner;
    private final String title;
    private ArrayList<FireCloudUser> guests;
    private DateFormat start_date_time;
    private DateFormat end_date_time;
    private final String localization;
    private final LatLng position;


    public FireCloudEvent(int id, String owner, String title, ArrayList<FireCloudUser> guests, DateFormat start_date_time, DateFormat end_date_time, String localization, LatLng position) {
        this.id = id;
        this.owner = owner;
        this.title = title;
        this.guests = guests;
        this.start_date_time = start_date_time;
        this.end_date_time = end_date_time;
        this.localization = localization;
        this.position = position;
    }

    protected FireCloudEvent(Parcel in) {
        Double lat = in.readDouble();
        Double lon = in.readDouble();
        position = new LatLng(lat, lon);
        title = in.readString();
        localization = in.readString();
        owner = in.readString();
        id = in.readInt();
        guests = new ArrayList<>();
        guests = in.readArrayList(FireCloudUser.class.getClassLoader());
    }

    public static final Creator<FireCloudEvent> CREATOR = new Creator<FireCloudEvent>() {
        @Override
        public FireCloudEvent createFromParcel(Parcel in) {
            return new FireCloudEvent(in);
        }

        @Override
        public FireCloudEvent[] newArray(int size) {
            return new FireCloudEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(position.latitude);
        dest.writeDouble(position.longitude);
        dest.writeString(title);
        dest.writeString(localization);
        dest.writeString(owner);
        dest.writeInt(id);
        dest.writeValue(guests);
    }

    public int getId() {
        return id;
    }

    public String getOwner() { return owner; }

    public String getTitle() { return title; }

    public ArrayList<FireCloudUser> getGuests() { return guests; }

    public void setGuests(ArrayList<FireCloudUser> guests) { this.guests = guests; }

    public DateFormat getStart_date_time() { return start_date_time; }

    public void setStart_date_time(DateFormat start_date_time) { this.start_date_time = start_date_time; }

    public DateFormat getEnd_date_time() { return end_date_time; }

    public void setEnd_date_time(DateFormat end_date_time) { this.end_date_time = end_date_time; }

    public String getLocalization() { return localization; }

    public LatLng getPosition() { return position; }
}
