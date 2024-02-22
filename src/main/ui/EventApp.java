package ui;

import model.Administrator;
import model.Event;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Event organizing application
public class EventApp {
    private static final String JSON_STORE = "./data/events.json";
    private static int eventID = 1;                                               // ID of an event
    private static Administrator administrator;
    private static JsonReader jsonReader;
    private static JsonWriter jsonWriter;


    // EFFECTS: runs the event application
    public EventApp() {
        administrator = new Administrator();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runEvent();
    }

    // EFFECTS: processes user input
    private void runEvent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n" + "Welcome to the Sports Event Management System" + "\n");
        while (true) {
            mainMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    administratorMenu(administrator, scanner);
                    break;
                case 2:
                    playerMenu(administrator, scanner);
                    break;
                case 3:
                    loadEvents();
                    break;
                case 4:
                    System.out.println("\n" + "Have a nice day!");
                    System.exit(0);
                default:
                    System.out.println("\n" + "Invalid choice. Try again.");
            }
        }
    }

    private static void mainMenu() {
        System.out.println("\n" + "1. Administrator");
        System.out.println("2. Player");
        System.out.println("3. Load events");
        System.out.println("4. Exit");
        System.out.print("\n" + "Select user type: ");
    }

    // EFFECTS: processes administrator command
    @SuppressWarnings("methodlength")
    public static void administratorMenu(Administrator administrator, Scanner scanner) {
        while (true) {
            menuAdministrator();
            int adminChoice = scanner.nextInt();
            switch (adminChoice) {
                case 1:
                    createEvent(administrator, scanner);
                    break;
                case 2:
                    modifyEvent(administrator, scanner);
                    break;
                case 3:
                    deleteEvent(administrator, scanner);
                    break;
                case 4:
                    viewEvents();
                    break;
                case 5:
                    saveEvents();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("\n" + "Invalid choice. Try again.");
            }
        }
    }

    // EFFECTS: displays administrator menu
    private static void menuAdministrator() {
        System.out.println("\n" + "Administrator Menu:");
        System.out.println("1. Create an event");
        System.out.println("2. Modify an event");
        System.out.println("3. Delete an event");
        System.out.println("4. View events");
        System.out.println("5. Save events");
        System.out.println("6. Back to main menu");
        System.out.print("\n" + "Enter your choice: ");
    }

    // EFFECTS: processes player command
    @SuppressWarnings("methodlength")
    public static void playerMenu(Administrator administrator, Scanner scanner) {
        System.out.print("\n" + "Enter your name: ");
        String playerName = scanner.next();

        Player player = checkPlayer(administrator, playerName);

        if (player == null) {
            player = new Player(playerName);
        }
        // Player found in the list of all players, proceed with the player's menu
        while (true) {
            menuPlayer();
            int playerChoice = scanner.nextInt();
            switch (playerChoice) {
                case 1:
                    registerForEvent(administrator, player, scanner);
                    break;
                case 2:
                    optOutOfEvent(administrator, player, scanner);
                    break;
                case 3:
                    viewRegisteredEvents(player);
                    break;
                case 4:
                    saveEvents();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("\n" + "Invalid choice. Try again.");
            }
        }
    }

    // EFFECTS: checks to see if player exists in the list
    private static Player checkPlayer(Administrator administrator, String playerName) {
        ArrayList<Event> events = administrator.getEvents();
        Player player = null;

        // Collect all players from all events
        List<Player> allPlayers = new ArrayList<>();
        for (Event event : events) {
            allPlayers.addAll(event.getPlayers());
        }

        // Check if the playerName is in the list of all players
        for (Player p : allPlayers) {
            if (p.getName().equals(playerName)) {
                player = p;
                break; // Player found, exit the loop
            }
        }
        return player;
    }

    // EFFECTS: displays player menu
    private static void menuPlayer() {
        System.out.println("\n" + "Player Menu:");
        System.out.println("1. Register for an event");
        System.out.println("2. Opt out of an event");
        System.out.println("3. View registered events");
        System.out.println("4. Save events");
        System.out.println("5. Back to the main menu");
        System.out.print("\n" + "Enter your choice: ");
    }

    /*
     * EFFECTS: creates an event from user input
     */
    public static void createEvent(Administrator administrator, Scanner scanner) {
        System.out.print("\n" + "Enter sport: ");
        String sport = scanner.next();
        System.out.print("Enter required players: ");
        int requiredPlayers = scanner.nextInt();
        System.out.print("Enter location: ");
        String location = scanner.next();
        if (administrator.getEvents().size() > 0) {
            int eventNum = administrator.getEvents().get(administrator.getEvents().size() - 1).getEventID();
            eventID = eventNum + 1;
        }
        Event event = new Event(eventID, sport, requiredPlayers, location);
        if (administrator.createEvent(event)) {
            eventID++;
            System.out.println("\n" + "Event created successfully." + "\n");
        }
    }

    /*
     * EFFECTS: modifies event details as per user input
     */
    public static void modifyEvent(Administrator administrator, Scanner scanner) {
        System.out.print("\n" + "Enter event ID to modify: " + "\n");
        int eventID = scanner.nextInt();
        Event event = administrator.findEventById(eventID);
        if (event != null) {
            System.out.print("Enter new sport (or press Enter to keep existing): ");
            String sport = scanner.nextLine();
            administrator.modifyEvent(event, sport, "", "");

            System.out.print("Enter new required players (or press Enter to keep existing): ");
            String playersInput = scanner.nextLine().trim();
            administrator.modifyEvent(event, sport, playersInput, "");

            System.out.print("Enter new location (or press Enter to keep existing): ");
            String location = scanner.nextLine().trim();
            administrator.modifyEvent(event, sport, playersInput, location);

            System.out.println("\n" + "Event modified successfully." + "\n");
        } else {
            System.out.println("\n" + "Event not found." + "\n");
        }
    }

    /*
     * EFFECTS: deletes an event the user requests
     */
    public static void deleteEvent(Administrator administrator, Scanner scanner) {
        System.out.print("\n" + "Enter event ID to delete: ");
        int eventID = scanner.nextInt();
        int returnVal = administrator.deleteEvent(eventID, administrator);

        if (returnVal == 1) {
            System.out.println("\n" + "Event deleted successfully." + "\n");
        } else {
            System.out.println("\n" + "Event not found." + "\n");
        }
    }

    /*
     * EFFECTS: shows the user the list of events
     */
    public static void viewEvents() {
        if (administrator.getEvents().isEmpty()) {
            System.out.println("\n" + "There are no events." + "\n");
        } else {
            for (Event event : administrator.getEvents()) {
                System.out.println(event);
            }
        }
    }

    /*
     * EFFECTS: registers the player to their event of choice
     */
    public static void registerForEvent(Administrator administrator, Player pl, Scanner scanner) {
        System.out.print("\n" + "Enter event ID to register for: ");
        int eventID = scanner.nextInt();
        int returnVal = pl.registerForEvent(administrator, eventID, pl);

        if (returnVal == 1) {
            System.out.println("\n" + "Registered for the event successfully!");
        } else if (returnVal == 2) {
            System.out.println("\n" + "There is no space!");
        } else {
            System.out.println("\n" + "Event not found or you are already registered in the event.");
        }
    }

    /*
     * EFFECTS: removes the player from the event
     */
    public static void optOutOfEvent(Administrator administrator, Player pl, Scanner scanner) {
        System.out.print("\n" + "Enter event ID to opt out of: ");
        int eventID = scanner.nextInt();
        int returnVal = pl.optOutOfEvent(administrator, eventID, pl);

        if (returnVal == 1) {
            System.out.println("\n" + "Opted out of the event successfully.");
        } else if (returnVal == 2) {
            System.out.println("\n" + "Player not found in the event.");
        } else {
            System.out.println("\n" + "Event not found.");
        }
    }

    /*
     * EFFECTS: shows the list of events the player is in
     */
    public static void viewRegisteredEvents(Player pl) {
        // work with the player name and not the player and set the name to the player
        if (pl.getRegisteredEvents().size() > 0) {
            for (Event event : pl.getRegisteredEvents()) {
                System.out.println(event);
            }
        } else {
            System.out.println("\n" + "You are not registered in any events.");
        }
    }

    // EFFECTS: saves the workroom to file
    private static void saveEvents() {
        try {
            jsonWriter.open();
            jsonWriter.writeEvents(administrator.getEvents());
            jsonWriter.close();
            System.out.println("Saved events to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private static void loadEvents() {
        try {
            ArrayList<Event> loadedEvents = jsonReader.readEvents();
            administrator.setEvents(loadedEvents);
            System.out.println("Loaded events from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
