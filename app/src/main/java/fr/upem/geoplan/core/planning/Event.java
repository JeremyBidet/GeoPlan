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

    private final long id = 0;
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

}
