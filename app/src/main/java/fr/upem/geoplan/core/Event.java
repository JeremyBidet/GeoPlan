package fr.upem.geoplan.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Event implements Parcelable {
    public final LatLng position;

    public Event(Double lat, Double lon) {
        position = new LatLng(lat, lon);
    }

    protected Event(Parcel in) {
        Double lat = in.readDouble();
        Double lon = in.readDouble();
        position = new LatLng(lat, lon);
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
    }
}
