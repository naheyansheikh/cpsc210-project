package model;

import java.util.ArrayList;
import java.util.List;

// Represents an event with its event ID, sport, required players, location, and list of players in the event

public class Event {
    private final int eventID;                                  // ID that is associated with an event
    private String sport;                                       // the sport being hosted
    private int requiredPlayers;                                // the number of players needed for the event
    private String location;                                    // the location of the event
    private final List<Player> players = new ArrayList<>();     // the list of players in the event


    /*
     * REQUIRES: requiredPlayers > 0 and eventID > 0
     * EFFECTS: the creation of an event from user input
     */
    public Event(int eventID, String sport, int requiredPlayers, String location) {
        this.eventID = eventID;
        this.sport = sport;
        this.requiredPlayers = requiredPlayers;
        this.location = location;
    }

    public int getEventID() {
        return eventID;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getRequiredPlayers() {
        return requiredPlayers;
    }

    public void setRequiredPlayers(int requiredPlayers) {
        this.requiredPlayers = requiredPlayers;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPlayers(ArrayList<Player> pls, Event event) {
        for (Player p : pls) {
            p.addRegisteredEvents(event);
            players.add(p);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds players to the event
     */
    public boolean addPlayer(Player player) {
        if (requiredPlayers == 0) {
            return false;
        } else {
            players.add(player);
            requiredPlayers--;
            return true;
        }
    }

    /*
     * MODIFIES: this
     * EFFECTS: removes player from the event
     */
    public boolean removePlayer(String name) {
        boolean removed = false;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(name)) {
                players.remove(i);
                removed = true;
                requiredPlayers++;
            }
        }
        return removed;
    }

    /*
     * EFFECTS: returns a string representation of event
     */
    @Override
    public String toString() {
        StringBuilder playerNames = new StringBuilder();
        for (Player player : players) {
            playerNames.append(player.getName()).append(", ");
        }
        return "\n" + "Event ID: " + eventID + "\n"
                + "Sport: " + sport + "\n"
                + "Required Players: " + requiredPlayers + "\n"
                + "Location: " + location + "\n"
                + "Players: " + (players.isEmpty() ? "None" : playerNames.toString()) + "\n";
    }
}


