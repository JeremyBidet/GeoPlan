package fr.upem.firecloud;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.ArrayList;

public class FireCloudEvent {
    private final long id;
    private String title;
    private String description;
    private String localization;
    private LatLng position;
    private Date start_date_time;
    private Date end_date_time;
    private final ArrayList<FireCloudUser> owners;
    private final ArrayList<FireCloudUser> guests = new ArrayList<>();


    public FireCloudEvent(long id, ArrayList<FireCloudUser> owners, String title,String description, ArrayList<FireCloudUser> guests, Date start_date_time, Date end_date_time, String localization, LatLng position) {
        this.id = id;
        this.owners = owners;
        this.title = title;
        this.description = description;
        this.guests.addAll(guests);
        this.start_date_time = start_date_time;
        this.end_date_time = end_date_time;
        this.localization = localization;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public ArrayList<FireCloudUser> getOwners() { return owners; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public ArrayList<FireCloudUser> getGuests() { return guests; }

    public void setGuests(ArrayList<FireCloudUser> guests) {
        this.guests.clear();
        this.guests.addAll(guests);
    }

    public Date getStart_date_time() { return start_date_time; }

    public void setStart_date_time(Date start_date_time) { this.start_date_time = start_date_time; }

    public Date getEnd_date_time() { return end_date_time; }

    public void setEnd_date_time(Date end_date_time) { this.end_date_time = end_date_time; }

    public String getLocalization() { return localization; }

    public void setLocalization(String localization) { this.localization = localization;}

    public LatLng getPosition() { return position; }

    public void setPosition(LatLng position) { this.position = position; }
}
