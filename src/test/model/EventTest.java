package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Administrator testAdministrator;
    private Event testEvent1;
    private Event testEvent2;
    private Event testEvent3;
    private Player testPlayer1;
    private Player testPlayer2;

    @BeforeEach
    void runBefore() {
        testAdministrator = new Administrator();
        testEvent1 = new Event(1, "Soccer", 11, "UBC");
        testEvent2 = new Event(2, "Tennis", 2, "UVIC");
        testEvent3 = new Event(3, "Fencing", 0, "UOFC");
        testPlayer1 = new Player("Ken");
        testPlayer2 = new Player("Barbie");
    }

    @Test
    void testAdministratorConstructor() {
        ArrayList<Event> allEvents = testAdministrator.getEvents();
        assertEquals(0, allEvents.size());
    }

    @Test
    void testEventConstructor() {
        assertEquals(1, testEvent1.getEventID());
        assertEquals("Soccer", testEvent1.getSport());
        assertEquals(11, testEvent1.getRequiredPlayers());
        assertEquals("UBC", testEvent1.getLocation());
        List<Player> allPlayers1 = testEvent1.getPlayers();
        assertEquals(0, allPlayers1.size());

        assertEquals(2, testEvent2.getEventID());
        assertEquals("Tennis", testEvent2.getSport());
        assertEquals(2, testEvent2.getRequiredPlayers());
        assertEquals("UVIC", testEvent2.getLocation());
        List<Player> allPlayers2 = testEvent2.getPlayers();
        assertEquals(0, allPlayers2.size());
    }

    @Test
    void testPlayerConstructor() {
        assertEquals("Ken", testPlayer1.getName());
        ArrayList<Event> registeredEvents1 = testPlayer1.getRegisteredEvents();
        assertEquals(0, registeredEvents1.size());

        assertEquals("Barbie", testPlayer2.getName());
        ArrayList<Event> registeredEvents2 = testPlayer2.getRegisteredEvents();
        assertEquals(0, registeredEvents2.size());
    }

    @Test
    void testCreateEventOnce() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        ArrayList<Event> allEvents = testAdministrator.getEvents();
        assertEquals(1, allEvents.size());
        assertTrue(allEvents.contains(testEvent1));
    }

    @Test
    void testCreateEventMultiple() {
        boolean addedOnce = testAdministrator.createEvent(testEvent1);
        assertTrue(addedOnce);
        boolean addedTwice = testAdministrator.createEvent(testEvent2);
        assertTrue(addedTwice);
        ArrayList<Event> allEvents = testAdministrator.getEvents();
        assertEquals(2, allEvents.size());
        assertTrue(allEvents.contains(testEvent1));
        assertTrue(allEvents.contains(testEvent2));
    }

    @Test
    void testModifyEvent() {
        boolean added = testAdministrator.createEvent(testEvent2);
        assertTrue(added);
        testAdministrator.modifyEvent(testEvent2, "Swimming", "", "");
        ArrayList<Event> allEvents = testAdministrator.getEvents();
        assertEquals("Swimming", allEvents.get(0).getSport());
        assertEquals(2, allEvents.get(0).getRequiredPlayers());
        assertEquals("UVIC", allEvents.get(0).getLocation());
        List<Player> allPlayers = allEvents.get(0).getPlayers();
        assertEquals(0, allPlayers.size());

        testAdministrator.modifyEvent(testEvent2, "", "4", "");
        assertEquals("Swimming", allEvents.get(0).getSport());
        assertEquals(4, allEvents.get(0).getRequiredPlayers());
        assertEquals("UVIC", allEvents.get(0).getLocation());

        testAdministrator.modifyEvent(testEvent2, "", "", "UOFT");
        assertEquals("Swimming", allEvents.get(0).getSport());
        assertEquals(4, allEvents.get(0).getRequiredPlayers());
        assertEquals("UOFT", allEvents.get(0).getLocation());
    }

    @Test
    void testDeleteEventNoEvent() {
        ArrayList<Event> events = testAdministrator.getEvents();
        assertEquals(0, events.size());
        int retVal = testAdministrator.deleteEvent(testEvent2.getEventID(), testAdministrator);
        assertEquals(2, retVal);
    }

    @Test
    void testDeleteEventOnce() {
        boolean added = testAdministrator.createEvent(testEvent2);
        assertTrue(added);
        int addedPlayer = testPlayer1.registerForEvent(testAdministrator, testEvent2.getEventID(), testPlayer1);
        assertEquals(1, addedPlayer);
        List<Event> playerEvents = testPlayer1.getRegisteredEvents();
        assertEquals(1, playerEvents.size());
        assertTrue(playerEvents.contains(testEvent2));
        ArrayList<Event> events = testAdministrator.getEvents();
        assertEquals(1, events.size());
        int retVal = testAdministrator.deleteEvent(testEvent2.getEventID(), testAdministrator);
        assertEquals(1, retVal);
        playerEvents = testPlayer1.getRegisteredEvents();
        assertEquals(0, playerEvents.size());
        assertFalse(playerEvents.contains(testEvent2));
    }

    @Test
    void testDeleteEventTwice() {
        boolean addedOnce = testAdministrator.createEvent(testEvent1);
        assertTrue(addedOnce);
        boolean addedTwice = testAdministrator.createEvent(testEvent2);
        assertTrue(addedTwice);
        ArrayList<Event> events = testAdministrator.getEvents();
        assertEquals(2, events.size());
        int retVal = testAdministrator.deleteEvent(testEvent1.getEventID(), testAdministrator);
        assertEquals(1, retVal);
        assertEquals(1, events.size());
        retVal = testAdministrator.deleteEvent(testEvent2.getEventID(), testAdministrator);
        assertEquals(1, retVal);
        assertEquals(0, events.size());
    }

    @Test
    void testFindEventByIdNoEvent() {
        Event event = testAdministrator.findEventById(1);
        assertNull(event);
    }

    @Test
    void testFindEventById() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        Event event = testAdministrator.findEventById(1);
        assertEquals(testEvent1, event);
    }

    @Test
    void testAddPlayerNoSpace() {
        List<Player> allPlayers = testEvent3.getPlayers();
        assertFalse(allPlayers.contains(testPlayer1));
        int requiredPlayers = testEvent3.getRequiredPlayers();
        assertEquals(0, requiredPlayers);

        boolean addedPlayer = testEvent3.addPlayer(testPlayer1);
        assertFalse(addedPlayer);
        allPlayers = testEvent3.getPlayers();
        requiredPlayers = testEvent3.getRequiredPlayers();
        assertFalse(allPlayers.contains(testPlayer1));
        assertEquals(0, requiredPlayers);
    }

    @Test
    void testAddPlayerSpaceAvailable() {
        List<Player> allPlayers = testEvent1.getPlayers();
        assertFalse(allPlayers.contains(testPlayer1));
        int requiredPlayers = testEvent1.getRequiredPlayers();
        assertEquals(11, requiredPlayers);

        boolean addedPlayer = testEvent1.addPlayer(testPlayer1);
        assertTrue(addedPlayer);
        allPlayers = testEvent1.getPlayers();
        requiredPlayers = testEvent1.getRequiredPlayers();
        assertTrue(allPlayers.contains(testPlayer1));
        assertEquals(10, requiredPlayers);
    }

    @Test
    void testToStringNoPlayer() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        assertTrue(testEvent1.toString().contains("\n" + "Event ID: " + testEvent1.getEventID() + "\n"
                + "Sport: " + testEvent1.getSport() + "\n"
                + "Required Players: " + testEvent1.getRequiredPlayers() + "\n"
                + "Location: " + testEvent1.getLocation() + "\n"
                + "Players: " + "None" + "\n"));
    }

    @Test
    void testToStringWithPlayer() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        boolean addedPlayer = testEvent1.addPlayer(testPlayer1);
        assertTrue(addedPlayer);
        assertTrue(testEvent1.toString().contains("\n" + "Event ID: " + testEvent1.getEventID() + "\n"
                + "Sport: " + testEvent1.getSport() + "\n"
                + "Required Players: " + testEvent1.getRequiredPlayers() + "\n"
                + "Location: " + testEvent1.getLocation() + "\n"
                + "Players: " + testPlayer1.getName() + ", " + "\n"));
    }

    @Test
    void testRegisterForEventSuccess() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        int retVal = testPlayer1.registerForEvent(testAdministrator, testEvent1.getEventID(), testPlayer1);
        assertEquals(1, retVal);
    }

    @Test
    void testRegisterForEventNoSpace() {
        boolean added = testAdministrator.createEvent(testEvent3);
        assertTrue(added);
        int retVal = testPlayer1.registerForEvent(testAdministrator, testEvent3.getEventID(), testPlayer1);
        assertEquals(2, retVal);
    }

    @Test
    void testRegisterForEventAlreadyIn() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        int retVal = testPlayer1.registerForEvent(testAdministrator, testEvent1.getEventID(), testPlayer1);
        int retVal2 = testPlayer1.registerForEvent(testAdministrator, testEvent1.getEventID(), testPlayer1);
        assertEquals(1, retVal);
        assertEquals(3, retVal2);
    }

    @Test
    void testRegisterForEventNoEvent() {
        int retVal = testPlayer1.registerForEvent(testAdministrator, 4, testPlayer1);
        assertEquals(2, retVal);
    }

    @Test
    void testOptOutOfEventSuccess() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        int retVal = testPlayer1.registerForEvent(testAdministrator, testEvent1.getEventID(), testPlayer1);
        assertEquals(1, retVal);
        int retVal2 = testPlayer1.optOutOfEvent(testAdministrator, testEvent1.getEventID(), testPlayer1);
        assertEquals(1, retVal2);
    }

    @Test
    void testOptOutOfNotInEvent() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        int retVal = testPlayer1.registerForEvent(testAdministrator, testEvent1.getEventID(), testPlayer1);
        assertEquals(1, retVal);
        int retVal2 = testPlayer2.optOutOfEvent(testAdministrator, testEvent1.getEventID(), testPlayer2);
        assertEquals(2, retVal2);
    }

    @Test
    void testOptOutOfNoEvent() {
        boolean added = testAdministrator.createEvent(testEvent1);
        assertTrue(added);
        int retVal = testPlayer1.optOutOfEvent(testAdministrator, 4, testPlayer1);
        assertEquals(3, retVal);
    }

    @Test
    void testSetName() {
        testPlayer1.setName("Barbie");
        assertEquals("Barbie", testPlayer1.getName());
    }

    @Test
    void testSetEvents() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(testEvent1);
        events.add(testEvent2);
        testAdministrator.setEvents(events);
    }
}
