package model;

import java.util.ArrayList;

// Represents an administrator with the list of events they are in charge of
public class Administrator {
    private static ArrayList<Event> events;  // list of events that are taking place

    /*
     * EFFECTS: administrator with their list of events
     */
    public Administrator() {
        events = new ArrayList<>();
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    /*
     * REQUIRES: eventID > 0
     * EFFECTS: finds the corresponding event from its eventID
     */
    public Event findEventById(int eventID) {
        for (Event event : events) {
            if (event.getEventID() == eventID) {
                return event;
            }
        }
        return null;
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds the event that the user created into the events of all events
     */
    public boolean createEvent(Event event) {
        events.add(event);
        EventLog.getInstance().logEvent(new EventTwo("New event created: " + event.getEventID()));
        return true;
    }

    /*
     * MODIFIES: this
     * EFFECTS: modifies event details from user input
     */
    public void modifyEvent(Event event, String newSport, String newPlayers, String newLocation) {
        if (!newSport.isEmpty()) {
            event.setSport(newSport);
            EventLog.getInstance().logEvent(new EventTwo("Event name modified to: " + newSport));
        }
        if (!newPlayers.isEmpty()) {
            int requiredPlayers = Integer.parseInt(newPlayers);
            EventLog.getInstance().logEvent(new EventTwo("Event required players modified to: " + newPlayers));
            event.setRequiredPlayers(requiredPlayers);
        }
        if (!newLocation.isEmpty()) {
            event.setLocation(newLocation);
            EventLog.getInstance().logEvent(new EventTwo("Event location modified to: " + newLocation));
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: deletes an event the user requests
     */
    public int deleteEvent(int eventID, Administrator administrator) {
        Event event = findEventById(eventID);

        if (event != null) {
            for (int i = 0; i < event.getPlayers().size(); i++) {
                Player player = event.getPlayers().get(i);
                player.getRegisteredEvents().remove(event);
            }
            administrator.getEvents().remove(event);
            EventLog.getInstance().logEvent(new EventTwo("Event deleted: " + eventID));
            return 1;
        } else {
            EventLog.getInstance().logEvent(new EventTwo("Event not found: " + eventID));
            return 2;
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: sets the current list of events to a new list
     */
    public void setEvents(ArrayList<Event> event) {
        events = event;
    }
}
