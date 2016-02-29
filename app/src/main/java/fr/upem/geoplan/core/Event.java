package fr.upem.geoplan.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Event implements Parcelable {
    private final LatLng position;
    private final String title;

    public Event(LatLng position, String title) {
        this.position = position;
        this.title = title;
    }

    protected Event(Parcel in) {
        Double lat = in.readDouble();
        Double lon = in.readDouble();
        position = new LatLng(lat, lon);
        title = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
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
    }

    public String getTitle() {
        return title;
    }

    public LatLng getPosition() {
        return position;
    }
}
