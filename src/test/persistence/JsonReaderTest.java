package persistence;

import model.Event;
import model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ArrayList<Event> loadedEvents = reader.readEvents();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyEvents() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyEvents.json");
        try {
            ArrayList<Event> loadedEvents = reader.readEvents();
            assertEquals(0, loadedEvents.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralEvents() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralEvents.json");
        try {
            ArrayList<Event> loadedEvents = reader.readEvents();
            assertEquals(1, loadedEvents.size());
            assertEquals(1, loadedEvents.get(0).getEventID());
            assertEquals("soccer", loadedEvents.get(0).getSport());
            assertEquals(10, loadedEvents.get(0).getRequiredPlayers());
            assertEquals("ubc", loadedEvents.get(0).getLocation());

            List<Player> players = loadedEvents.get(0).getPlayers();
            assertEquals(1, players.size());
            assertEquals("naheyan", players.get(0).getName());

            ArrayList<Event> loadedEvents2 = reader.readEvents();
            assertEquals(1, loadedEvents2.size());
            assertEquals(1, loadedEvents2.get(0).getEventID());
            assertEquals("soccer", loadedEvents2.get(0).getSport());
            assertEquals(10, loadedEvents2.get(0).getRequiredPlayers());
            assertEquals("ubc", loadedEvents2.get(0).getLocation());

            List<Player> players2 = loadedEvents2.get(0).getPlayers();
            assertEquals(1, players2.size());
            assertEquals("naheyan", players2.get(0).getName());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
