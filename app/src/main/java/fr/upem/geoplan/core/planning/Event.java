package fr.upem.geoplan.core.planning;

import android.location.LocationListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jerem_000 on 11/02/2016.
 */
public class Event {

    private final long id;
    private String name;
    private DateFormat start_date_time;
    private DateFormat end_date_time;
    private String localization;
    private float pos_lat;
    private float pos_lon;
    private ArrayList<Long> guests;
    private ArrayList<Long> owners;
    private String description;
    private Weight weight;
    private Type type;

    private Event(long id, String name, DateFormat start_date_time, DateFormat end_date_time, String localization, float pos_lat, float pos_lon, ArrayList<Long> guests, ArrayList<Long> owners, String description, Weight weight, Type type) {
        this.id = id;
        this.name = name;
        this.start_date_time = start_date_time;
        this.end_date_time = end_date_time;
        this.localization = localization;
        this.pos_lat = pos_lat;
        this.pos_lon = pos_lon;
        this.guests = guests;
        this.owners = owners;
        this.description = description;
        this.weight = weight;
        this.type = type;
    }


    public static Event getEvent(long id) {
        //TODO: get from database the event by its event id

        return new Event(id, null, null, null, null, 0, 0, null, null, null, null, null);
    }

}
