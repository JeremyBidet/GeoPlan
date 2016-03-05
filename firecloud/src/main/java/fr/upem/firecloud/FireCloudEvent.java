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
    private final ArrayList<Long> ownersId;
    private final ArrayList<Long> guestsId = new ArrayList<>();


    public FireCloudEvent(long id, ArrayList<Long> ownersId, String title,String description, ArrayList<Long> guestsId, Date startDateTime, Date endDateTime, String localization, LatLng position) {
        this.id = id;
        this.ownersId = ownersId;
        this.title = title;
        this.description = description;
        this.guestsId.addAll(guestsId);
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.localization = localization;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public ArrayList<Long> getOwnersId() { return ownersId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public ArrayList<Long> getGuestsId() { return guestsId; }

    public void setGuestsId(ArrayList<Long> guestsId) {
        this.guestsId.clear();
        this.guestsId.addAll(guestsId);
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
