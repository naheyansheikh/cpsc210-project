package persistence;

import model.Event;
import model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Event event = new Event(1, "soccer", 5, "ubc");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyEvents() {
        try {
            Event event1 = new Event(1, "soccer", 5, "ubc");
            Player player = new Player("naheyan");
            event1.addPlayer(player) ;
            List<Event> event = new ArrayList<>();
            event.add(event1);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyEvents.json");
            writer.open();
            writer.writeEvents(event);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyEvents.json");
            event = reader.readEvents();
            assertEquals(1, event.size());
            assertEquals(1, event.get(0).getEventID());
            assertEquals("soccer", event.get(0).getSport());
            assertEquals(4, event.get(0).getRequiredPlayers());
            assertEquals("ubc", event.get(0).getLocation());

            List<Player> players = event.get(0).getPlayers();
            assertEquals(1, players.size());
            assertEquals("naheyan", players.get(0).getName());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralEvents() {
        try {
            JsonReader reader = new JsonReader("./data/testWriterGeneralEvents.json");
            List<Event> events = reader.readEvents();

            events.add(new Event(2, "soccer", 5, "ubc"));

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralEvents.json");
            writer.open();
            writer.writeEvents(events);
            writer.close();

            events = reader.readEvents();

            assertEquals(2, events.size());
            assertEquals(1, events.get(0).getEventID());
            assertEquals(2, events.get(1).getEventID());
            assertEquals("swimming", events.get(0).getSport());
            assertEquals("soccer", events.get(1).getSport());
            assertEquals(5, events.get(0).getRequiredPlayers());
            assertEquals(5, events.get(1).getRequiredPlayers());
            assertEquals("uvic", events.get(0).getLocation());
            assertEquals("ubc", events.get(1).getLocation());

            List<Player> players = events.get(0).getPlayers();
            assertEquals(0, players.size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
