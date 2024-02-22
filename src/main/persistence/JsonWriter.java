package persistence;

import model.Event;
import model.EventLog;
import model.EventTwo;
import model.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

// Represents a writer that writes JSON representation of events to file
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of events to file
    public void writeEvents(List<Event> events) {
        JSONArray jsonArray = new JSONArray();
        for (Event event : events) {
            jsonArray.put(eventToJson(event));
        }
        saveToFile(jsonArray.toString(TAB));
        EventLog.getInstance().logEvent(new EventTwo("All events saved to: " + destination));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: stores event details to JSON
    private JSONObject eventToJson(Event event) {
        JSONObject eventJson = new JSONObject();
        eventJson.put("eventID", event.getEventID());
        eventJson.put("sport", event.getSport());
        eventJson.put("requiredPlayers", event.getRequiredPlayers());
        eventJson.put("location", event.getLocation());

        JSONArray playersJson = new JSONArray();
        for (Player player : event.getPlayers()) {
            JSONObject playerJson = new JSONObject();
            playerJson.put("playerName", player.getName());
            playersJson.put(playerJson);
        }
        eventJson.put("players", playersJson);

        return eventJson;
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
