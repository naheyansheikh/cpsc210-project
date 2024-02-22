package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a log of alarm system eventTwos.
 * We use the Singleton Design Pattern to ensure that there is only
 * one EventLog in the system and that the system has global access
 * to the single instance of the EventLog.
 */
public class EventLog implements Iterable<EventTwo> {
    /**
     * the only EventLog in the system (Singleton Design Pattern)
     */
    private static EventLog theLog;
    private Collection<EventTwo> eventTwos;

    /**
     * Prevent external construction.
     * (Singleton Design Pattern).
     */
    private EventLog() {
        eventTwos = new ArrayList<>();
    }

    /**
     * Gets instance of EventLog - creates it
     * if it doesn't already exist.
     * (Singleton Design Pattern)
     *
     * @return instance of EventLog
     */
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }
        return theLog;
    }

    /**
     * Adds an event to the event log.
     *
     * @param e the event to be added
     */
    public void logEvent(EventTwo e) {
        eventTwos.add(e);
    }

    /**
     * ** Clears the event log and logs the event.
     */
    public void clear() {
        eventTwos.clear();
        logEvent(new EventTwo("EventTwo log cleared."));
    }

    @Override
    public Iterator<EventTwo> iterator() {
        return eventTwos.iterator();
    }
}
