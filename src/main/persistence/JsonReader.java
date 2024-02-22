package persistence;

import model.Event;
import model.EventLog;
import model.EventTwo;
import model.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads events from JSON data stored in file
public class JsonReader {
    private final String source;
    HashMap<String, Player> playerMap = new HashMap<>();


    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads events from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList<Event> readEvents() throws IOException {
        String jsonData = readFile(source);
        JSONArray jsonArray = new JSONArray(jsonData);
        EventLog.getInstance().logEvent(new EventTwo("Events loaded from: " + source));
        return parseEvents(jsonArray);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses events from JSON array and returns it
    private ArrayList<Event> parseEvents(JSONArray jsonArray) {
        ArrayList<Event> events = new ArrayList<>();

        for (Object json : jsonArray) {
            JSONObject eventJson = (JSONObject) json;
            Event event = parseEvent(eventJson);
            events.add(event);
        }

        return events;
    }

    // EFFECTS: parses an event from JSON object and returns it
    private Event parseEvent(JSONObject eventJson) {
        int eventID = eventJson.getInt("eventID");
        String sport = eventJson.getString("sport");
        int requiredPlayers = eventJson.getInt("requiredPlayers");
        String location = eventJson.getString("location");
        JSONArray playersJson = eventJson.getJSONArray("players");
        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < playersJson.length(); i++) {
            JSONObject playerJson = playersJson.getJSONObject(i);
            String playerName = playerJson.getString("playerName");
            Player player;

            if (playerMap.containsKey(playerName)) {
                player = playerMap.get(playerName);
            } else {
                player = new Player(playerName);
                playerMap.put(playerName, player);
            }

            players.add(player);
        }

        Event event = new Event(eventID, sport, requiredPlayers, location);
        event.setPlayers(players, event);

        return event;
    }


}
