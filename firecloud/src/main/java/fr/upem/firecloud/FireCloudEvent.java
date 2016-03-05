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
    private Date startDateTime;
    private Date endDateTime;
    private final ArrayList<FireCloudUser> owners;
    private final ArrayList<FireCloudUser> guests = new ArrayList<>();


    public FireCloudEvent(long id, ArrayList<FireCloudUser> owners, String title,String description, ArrayList<FireCloudUser> guests, Date startDateTime, Date endDateTime, String localization, LatLng position) {
        this.id = id;
        this.owners = owners;
        this.title = title;
        this.description = description;
        this.guests.addAll(guests);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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

    public Date getStartDateTime() { return startDateTime; }

    public void setStartDateTime(Date startDateTime) { this.startDateTime = startDateTime; }

    public Date getEndDateTime() { return endDateTime; }

    public void setEndDateTime(Date endDateTime) { this.endDateTime = endDateTime; }

    public String getLocalization() { return localization; }

    public void setLocalization(String localization) { this.localization = localization;}

    public LatLng getPosition() { return position; }

    public void setPosition(LatLng position) { this.position = position; }
}
