package fr.upem.geoplan.core.planning;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fr.upem.geoplan.core.session.User;

/**
 * Created by jerem_000 on 11/02/2016.
 */
public class Event implements Parcelable, Comparable<Event> {

    private final long id;
    private String name;
    private String description;
    private LatLng position;
    private String localization;
    private Date start_date_time;
    private Date end_date_time;
    private final ArrayList<User> guests = new ArrayList<>();
    private final ArrayList<User> owners = new ArrayList<>();
    private int weight;
    private String type;
    private float cost;
    private int color;

    public Event(long id, String name, String description, LatLng position, String localization,
                 Date start_date_time, Date end_date_time,
                 Collection<User> guests, Collection<User> owners,
                 int weight, String type, float cost, int color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
        this.localization = localization;
        this.start_date_time = start_date_time;
        this.end_date_time = end_date_time;
        this.guests.addAll(guests);
        this.owners.addAll(owners);
        this.weight = weight;
        this.type = type;
        this.cost = cost;
        this.color = color;
    }

    public Event(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.position = new LatLng(in.readDouble(), in.readDouble());
        this.localization = in.readString();
        this.start_date_time = new Date(in.readLong());
        this.end_date_time = new Date(in.readLong());
        in.readTypedList(guests, getUserCreator());
        in.readTypedList(owners, getUserCreator());
        this.weight = in.readInt();
        this.type = in.readString();
        this.cost = in.readFloat();
        this.color = in.readInt();
    }

    public Event(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeDouble(this.position.latitude);
        dest.writeDouble(this.position.longitude);
        dest.writeString(this.localization);
        dest.writeLong(this.start_date_time.getTime());
        dest.writeLong(this.end_date_time.getTime());
        dest.writeTypedList(this.guests);
        dest.writeTypedList(this.owners);
        dest.writeInt(this.weight);
        dest.writeString(this.type);
        dest.writeFloat(this.cost);
        dest.writeInt(this.color);
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Date getStart_date_time() { return this.start_date_time; }
    public void setStart_date_time(Date start_date_time) { this.start_date_time = start_date_time; }

    public Date getEnd_date_time() { return this.end_date_time; }
    public void setEnd_date_time(Date end_date_time) {
        this.end_date_time = end_date_time;
    }

    public String getLocalization() { return this.localization; }
    public void setLocalization(String localization) { this.localization = localization; }

    public LatLng getPosition() { return this.position; }
    public void setPos_lat(LatLng position) { this.position = position; }

    public List<User> getGuests() { return (List<User>) this.guests.clone(); }

    public List<User> getOwners() { return (List<User>) this.owners.clone(); }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return this.weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public float getCost() {
        return this.cost;
    }
    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getColor() { return this.color; }
    public void setColor(int color) { this.color = color; }

    protected Creator<User> getUserCreator() {
        return User.CREATOR;
    }

    @Override
    public String toString() {
        return name + " (" + position.latitude + ";" + position.longitude + ") - " + guests.size() + " guests";
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
    public boolean equals(Object o) {
        return o instanceof Event
                && ((Event) o).id == id;
    }

    @Override
    public int compareTo(Event another) {
        return this.start_date_time.before(another.getStart_date_time()) ?
                -1 :
                this.start_date_time.after(another.getStart_date_time()) ?
                        1 :
                        this.id == another.id ?
                            0 :
                            1;
    }


    /**
     * Initialize a temporary Event used to sort events in planning.<br/>
     * This Event cannot be added to the planning
     * @param start_date_time the start datetime used to compare with other events
     */
    Event(Date start_date_time) {
        this.id = -1;
        this.start_date_time = start_date_time;
    }

    //pour les tests
    public Event(long id, String name, Date start_date_time, Date end_date_time, String localization, int color) {
        this(id, name, "description", new LatLng(48.8392203, 2.5848739), localization,
                start_date_time, end_date_time, new ArrayList<User>(), new ArrayList<User>(),
                4, "type", 19.99f, color);
    }

}
