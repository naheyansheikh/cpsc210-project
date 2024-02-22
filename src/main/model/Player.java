package model;

import java.util.ArrayList;
// Represents a player with their name and list of events the player is in

public class Player {
    private final ArrayList<Event> registeredEvents = new ArrayList<>(); // the events the player is in
    private String name;                                                 // the name of the player

    /*
     * EFFECTS: a player with their name
     */
    public Player(String name) {
        this.name = name;
    }

    /*
     * EFFECTS: registers the player to their event of choice
     */
    public int registerForEvent(Administrator administrator, int eventID, Player pl) {
        Event event = administrator.findEventById(eventID);
        if ((event != null)) {
            if (event.getPlayers().contains(pl)) {
                EventLog.getInstance().logEvent(new EventTwo(pl.getName() + " is already registered in the event: "
                        + eventID));
                return 3;
            }
            if (event.addPlayer(pl)) {
                pl.addRegisteredEvents(event);
                EventLog.getInstance().logEvent(new EventTwo(pl.getName() + " added to Event: " + eventID));
                return 1;
            }
        }
        EventLog.getInstance().logEvent(new EventTwo("Event not found or is at capacity: "
                + eventID));
        return 2;
    }

    /*
     * EFFECTS: removes the player from the event
     */
    public int optOutOfEvent(Administrator administrator, int eventID, Player pl) {
        Event event = administrator.findEventById(eventID);
        if (event != null) {
            boolean optedOut = event.removePlayer(pl.getName());
            if (optedOut) {
                pl.getRegisteredEvents().remove(event);
                EventLog.getInstance().logEvent(new EventTwo("Player opted out of the event: " + eventID));
                return 1;
            } else {
                EventLog.getInstance().logEvent(new EventTwo("Player not found in the event: " + eventID));
                return 2;
            }
        } else {
            EventLog.getInstance().logEvent(new EventTwo("Event not found: " + eventID));
            return 3;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Event> getRegisteredEvents() {
        return registeredEvents;
    }

    public void addRegisteredEvents(Event event) {
        registeredEvents.add(event);
    }

}

