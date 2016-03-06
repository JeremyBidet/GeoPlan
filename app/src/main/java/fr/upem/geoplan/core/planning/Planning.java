package fr.upem.geoplan.core.planning;

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

    public Event getEventByID(long id) {
        for(Event e : this.events) {
            if(e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    public List<Event> getEvents() { return Arrays.asList((Event[]) this.events.toArray()); }

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

        Calendar c_today = Calendar.getInstance();
        c_today.setTime(start_date);
        int y = c_today.get(Calendar.YEAR), m = c_today.get(Calendar.MONTH), d = c_today.get(Calendar.DATE);
        c_today.clear();
        c_today.set(y, m, d);

        Calendar c_tomorrow = (Calendar) c_today.clone();
        c_tomorrow.add(Calendar.DATE, 1);

        Date today_date = c_today.getTime();
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
