package fr.upem.geoplan.core.planning;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jerem_000 on 11/02/2016.
 */
public class Planning {

    private final SortedSet<Event> events;
    private final LinkedList<Event> removed_events;

    public Planning() {
        this.events = new TreeSet< Event>();
        this.removed_events = new LinkedList<Event>();
    }

    public boolean addEvent(Event e) {
        if(e.getId() == -1) {
            return false;
        }
        return this.events.add(e);
    }

    /**
     * Get the position in the list of the first event which start after today (not today at 00:00 but current time).
     * @return the position in the list, or 0 if there is no event after today
     */
    public int getPosition() {
        return getPosition(getFirstEventToday());
    }

    /**
     * Get the position of this event in the list.
     * @param event the event to get the position from the list
     * @return the position in the list, or 0 if this event does not exists in the list
     */
    public int getPosition(Event event) {
        int i = 0;
        for(Event e : this.events) {
            if(e.equals(event)) {
                return i;
            }
            i++;
        }
        return 0;
    }

    private Event getFirstEventToday() {
        Date today = new Date();
        for(Event e : this.events) {
            if(e.getStart_date_time().after(today)) {
                return e;
            }
        }
        return null;
    }

    public Event getEventByID(long id) {
        for(Event e : this.events) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public List<Event> getEvents() {
        return Arrays.asList(this.events.toArray(new Event[this.events.size()]));
    }

    public List<Event> getEvents(String name) {
        List<Event> events = new LinkedList<Event>();
        for(Event e : this.events) {
            if(e.getName().contains(name)) {
                events.add(e);
            }
        }
        return events;
    }

    public List<Event> getEvents(int weight) {
        List<Event> events = new ArrayList<Event>();
        for(Event e : this.events) {
            if(e.getWeight() >= weight) {
                events.add(e);
            }
        }
        return events;
    }

    public Date getTodayDate() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR), m = c.get(Calendar.MONTH), d = c.get(Calendar.DATE);
        c.clear();
        c.set(y, m, d);
        return c.getTime();
    }

    public List<Event> getEventsFromToday() {
        return getEventsFromStartDate(getTodayDate());
    }

    public List<Event> getEventsUntilToday() {
        return getEventsUntilStartDate(getTodayDate());
    }

    public List<Event> getEventsFromStartDate(Date start_date) {
        return Arrays.asList((Event[]) this.events.tailSet(new Event(start_date)).toArray());
    }

    public List<Event> getEventsUntilStartDate(Date start_date) {
        return Arrays.asList((Event[]) this.events.headSet(new Event(start_date)).toArray());
    }

    public List<Event> getEventsToday() {
        return getEventsOnlyThisDay(getTodayDate());
    }

    public List<Event> getEventsOnlyThisDay(Date start_date) {
        List<Event> events = new ArrayList<>();

        Calendar c_day = Calendar.getInstance();
        c_day.setTime(start_date);
        int y = c_day.get(Calendar.YEAR), m = c_day.get(Calendar.MONTH), d = c_day.get(Calendar.DATE);
        c_day.clear();
        c_day.set(y, m, d);

        Calendar c_tomorrow = (Calendar) c_day.clone();
        c_tomorrow.add(Calendar.DATE, 1);

        Date today_date = c_day.getTime();
        Date tomorrow_date = c_tomorrow.getTime();

        return Arrays.asList((Event[]) this.events.subSet(new Event(today_date), new Event(tomorrow_date)).toArray());
    }

    public boolean removeEventById(long id) {
        Event e = getEventByID(id);
        if(e == null) {
            return false;
        }
        this.events.remove(e);
        this.removed_events.addLast(e);
        return true;
    }

    public boolean removeEvent(Event event) {
        if(this.events.remove(event)) {
            this.removed_events.addLast(event);
            return true;
        }
        return false;
    }

    public boolean removeAllEvent() {
        for(Event e : this.events) {
            this.removeEvent(e);
        }
        return this.events.size() == 0;
    }

    public Event getLastRemovedEvent() {
        return this.removed_events.getLast();
    }

    public LinkedList<Event> getRemovedEvent() {
        return this.removed_events;
    }

    public boolean restoreEvent(Event event) {
        if(this.events.add(event)) {
            this.removed_events.remove(event);
            return true;
        }
        return false;
    }

    public boolean restoreEvent(long id) {
        for(Event e : this.removed_events) {
            if(e.getId() == id) {
                restoreEvent(e);
            }
        }
        return false;
    }

}
