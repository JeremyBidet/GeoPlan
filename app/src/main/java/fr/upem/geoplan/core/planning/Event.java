package fr.upem.geoplan.core.planning;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

import fr.upem.geoplan.core.User;

/**
 * Created by jerem_000 on 11/02/2016.
 */
public class Event extends fr.upem.geoplan.core.Event {
    private Weight weight;
    private Type type;
    private int color;

    public Event(String name, Date start_date_time, Date end_date_time, String localization, LatLng position, ArrayList<User> guests, ArrayList<User> owners, String description, Weight weight, Type type, int color) {
        super(name, start_date_time, end_date_time, localization, position, guests, owners, description);

        this.weight = weight;
        this.type = type;
        this.color = color;
    }

    public Event(Parcel in) {
        super(in);

        color = in.readInt();
        //TODO get other fields
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeInt(color);
        //TODO write other fields
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

    protected Creator<User> getUserCreator() {
        return User.CREATOR;
    }

    //pour les tests
    public Event(String name, Date start_date_time, Date end_date_time, String localization, int color) {
        this(name, start_date_time, end_date_time, localization, new LatLng(48.8392203, 2.5848739), new ArrayList<User>(), new ArrayList<User>(), "", null, null, color);
    }

    public int getColor() {
        return this.color;
    }
}
