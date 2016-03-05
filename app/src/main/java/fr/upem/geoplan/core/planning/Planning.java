package fr.upem.geoplan.core.planning;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jerem_000 on 11/02/2016.
 */
public class Planning {

    private final ConcurrentHashMap<Long, Event> events;
    private final LinkedList<Event> removed_events;

    public Planning() {
        this.events = new ConcurrentHashMap<Long, Event>();
        this.removed_events = new LinkedList<Event>();
    }

    public Event getEventByID(long id) {
        return events.get(id);
    }

    public List<Event> getEventByName(String name) {
        List<Event> events = new ArrayList<Event>();
        for(Event e : this.events.values()) {
            if(e.getName().contains(name)) {
                events.add(e);
            }
        }
        return events;
    }

    public List<Event> getEventByStartDate(Date start_date) {
        List<Event> events = new ArrayList<Event>();
        for(Event e : this.events.values()) {
            if(e.getStart_date_time().after(start_date)) {
                events.add(e);
            }
        }
        return events;
    }

    public List<Event> getEventThisDay() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR), m = c.get(Calendar.MONTH), d = c.get(Calendar.DATE);
        c.clear();
        c.set(y, m, d);
        return getEventThisDay(c.getTime());
    }

    public List<Event> getEventThisDay(Date start_date) {
        List<Event> events = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        c.setTime(start_date);
        int y = c.get(Calendar.YEAR), m = c.get(Calendar.MONTH), d = c.get(Calendar.DATE);
        c.clear();
        c.set(y, m, d);
        Calendar cplus1 = (Calendar) c.clone();
        cplus1.set(Calendar.DATE, c.get(Calendar.DATE) + 1);

        Date date = c.getTime();
        Date dateplus1 = cplus1.getTime();

        for(Event e : this.events.values()) {
            if(e.getStart_date_time().after(date) && e.getStart_date_time().before(dateplus1)) {
                events.add(e);
            }
        }

        return events;
    }

    public List<Event> getEventByEndDate(Date end_date) {
        List<Event> events = new ArrayList<Event>();
        for(Event e : this.events.values()) {
            if(e.getEnd_date_time().after(end_date)) {
                events.add(e);
            }
        }
        return events;
    }

    public List<Event> getEventByWeight(int weight) {
        List<Event> events = new ArrayList<Event>();
        for(Event e : this.events.values()) {
            if(e.getWeight() >= weight) {
                events.add(e);
            }
        }
        return events;
    }

    public boolean removeEvent(long id) {
        Event e = this.events.remove(id);
        if(e == null) {
            return false;
        }
        this.removed_events.addLast(e);
        return true;
    }

    public boolean removeAllEvent() {
        for(Event e : this.events.values()) {
            this.removeEvent(e.getId());
        }
        return this.events.size() == 0;
    }

    public Event getLastRemovedEvent() {
        return this.removed_events.getLast();
    }

    public LinkedList<Event> getRemovedEvent() {
        return this.removed_events;
    }

    public boolean restoreEvent(Event e) {
        Event ev = this.events.put(e.getId(), e);
        if(ev == null) {
            return false;
        }
        this.removed_events.remove(e);
        return true;
    }

    public boolean restoreEvent(long id) {
        for(Event e : removed_events) {
            if(e.getId() == id) {
                restoreEvent(e);
            }
        }
        return false;
    }

}
