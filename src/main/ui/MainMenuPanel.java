package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Represents the main window in which the Sports Management System is displayed.
 */
public class MainMenuPanel extends JPanel {
    private static final String JSON_STORE = "./data/events.json";
    private static JsonReader jsonReader;
    private static JsonWriter jsonWriter;
    private final JFrame mainFrame;
    private static Administrator administrator;
    private static int eventID = 1;                     // ID of an event
    private static Color textColour;                   // colour of every text field

    // EFFECTS: sets up window in which Sports Management System is displayed.
    public MainMenuPanel(JFrame mainFrame) {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        administrator = new Administrator();
        this.mainFrame = mainFrame;
        textColour = new Color(253, 253, 236);
        mainFrame.setLocationRelativeTo(null); // Center on screen
        checkLoadEvents();
        initializeUI();

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                printLog(EventLog.getInstance());
            }
        });
    }

    // EFFECTS: sets up the main menu
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 45, 77));
        mainFrame.setSize(600, 500);
        mainFrame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Sports Event Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(textColour);
        add(titleLabel, BorderLayout.NORTH);

        ImageIcon imageIcon = new ImageIcon("data/mainPagePhoto.png");

        JLabel imageLabel = new JLabel(imageIcon);
        add(imageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
        buttonPanel.setBackground(new Color(34, 45, 77));

        mainPageButtons(buttonPanel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // EFFECTS: creates the buttons for the main page
    private void mainPageButtons(JPanel buttonPanel) {
        JButton adminButton = createMainMenuButton("Administrator");
        adminButton.addActionListener(e -> {
            openAdministratorMenu(mainFrame);
            mainFrame.setVisible(false);
        });

        JButton playerButton = createMainMenuButton("Player");
        playerButton.addActionListener(e -> {
            openPlayerLoginPanel(mainFrame);
            mainFrame.setVisible(false);
        });

        JButton exitButton = createMainMenuButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(adminButton);
        buttonPanel.add(playerButton);
        buttonPanel.add(exitButton);
    }

    // EFFECTS: creates a button for the main menu
    private JButton createMainMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 50));
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        return button;
    }

    // EFFECTS: sets up the Administrator menu
    private static void openAdministratorMenu(JFrame mainFrame) {
        JFrame adminFrame = new JFrame("Administrator Menu");
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setSize(400, 300);
        adminFrame.setLocationRelativeTo(null);

        JPanel adminPanel = new JPanel(new GridBagLayout());
        adminPanel.setBackground(new Color(34, 45, 77));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        adminMenuTitle(adminPanel, gbc);
        JButton createEventButton = adminMenuCreateEventButton(adminFrame);
        JButton modifyEventButton = adminMenuModifyButton(adminFrame);
        JButton viewEventsButton = adminMenuViewButton(adminFrame);
        JButton saveEventsButton = adminMenuSaveButton();
        JButton deleteEventsButton = adminMenuDeleteButton(adminFrame);
        JButton backButton = adminMenuBackButton(mainFrame, adminFrame);

        adminMenuLayout(adminPanel, gbc, createEventButton, modifyEventButton, viewEventsButton, saveEventsButton,
                deleteEventsButton, backButton);

        adminFrame.setContentPane(adminPanel);
        adminFrame.setVisible(true);
    }

    // EFFECTS: creates the title for the admin menu
    private static void adminMenuTitle(JPanel adminPanel, GridBagConstraints gbc) {
        JLabel titleLabel = new JLabel("Administrator Menu");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        adminPanel.add(titleLabel, gbc);
    }

    // EFFECTS: creates the create event button for the admin menu
    private static JButton adminMenuCreateEventButton(JFrame adminFrame) {
        JButton createEventButton = createButton("Create Event");
        createEventButton.addActionListener(e -> {
            openCreateEventPanel(adminFrame);
            adminFrame.setVisible(false);
        });
        return createEventButton;
    }

    // EFFECTS: creates the modify event button for the admin menu
    private static JButton adminMenuModifyButton(JFrame adminFrame) {
        JButton modifyEventButton = createButton("Modify Event");
        modifyEventButton.addActionListener(e -> openModifyEventPanel(adminFrame));
        return modifyEventButton;
    }

    // EFFECTS: creates the view event button for the admin menu
    private static JButton adminMenuViewButton(JFrame adminFrame) {
        JButton viewEventsButton = createButton("View Events");
        viewEventsButton.addActionListener(e -> {
            openViewEventsPanel(adminFrame);
            adminFrame.setVisible(false);
        });
        return viewEventsButton;
    }

    // EFFECTS: creates the save event button for the admin menu
    private static JButton adminMenuSaveButton() {
        JButton saveEventsButton = createButton("Save Events");
        saveEventsButton.addActionListener(e -> saveEvents());
        return saveEventsButton;
    }

    // EFFECTS: creates the delete event button for the admin menu
    private static JButton adminMenuDeleteButton(JFrame adminFrame) {
        JButton deleteEventsButton = createButton("Delete Events");
        deleteEventsButton.addActionListener(e -> {
            deleteEvents(adminFrame);
            adminFrame.setVisible(false);
        });
        return deleteEventsButton;
    }

    // EFFECTS: creates the back button for the admin menu
    private static JButton adminMenuBackButton(JFrame mainFrame, JFrame adminFrame) {
        JButton backButton = createButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            adminFrame.dispose();
            mainFrame.setVisible(true);
        });
        return backButton;
    }

    // EFFECTS: creates the layout for the administrator menu
    private static void adminMenuLayout(JPanel adminPanel, GridBagConstraints gbc, JButton createEventButton,
                                        JButton modifyEventButton, JButton viewEventsButton, JButton saveEventsButton,
                                        JButton deleteEventsButton, JButton backButton) {
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        adminPanel.add(createEventButton, gbc);

        gbc.gridx = 1;
        adminPanel.add(modifyEventButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        adminPanel.add(viewEventsButton, gbc);

        gbc.gridx = 1;
        adminPanel.add(saveEventsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        adminPanel.add(deleteEventsButton, gbc);

        gbc.gridx = 1;
        adminPanel.add(backButton, gbc);
    }

    // EFFECTS: creates a button for the project
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(160, 50));
        return button;
    }

    // MODIFIES: this
    // EFFECTS: the panel where creating an event takes place
    private static void openCreateEventPanel(JFrame adminFrame) {
        JFrame createEventFrame = new JFrame("Create Event");
        createEventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createEventFrame.setSize(500, 400);
        createEventFrame.setLocationRelativeTo(null);

        JPanel createEventPanel = new JPanel(new GridBagLayout());
        createEventPanel.setBackground(new Color(34, 45, 77));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        createEventTitle(createEventPanel, gbc);

        JLabel sportLabel = createLabel("Sport:");
        JTextField sportField = createTextField();

        JLabel playersLabel = createLabel("Players Needed:");
        JTextField playersField = createTextField();

        JLabel locationLabel = createLabel("Location:");
        JTextField locationField = createTextField();

        JButton createEventButton = createEventButton(adminFrame, createEventFrame, sportField, playersField,
                locationField);

        JButton backButton = createEventBackButton(adminFrame, createEventFrame);

        createEventLayout(createEventPanel, gbc, sportLabel, sportField, playersLabel, playersField, locationLabel,
                locationField, createEventButton, backButton);

        createEventFrame.setContentPane(createEventPanel);
        createEventFrame.setVisible(true);
    }

    // EFFECTS: creates the create event button for the create event panel
    private static JButton createEventButton(JFrame adminFrame, JFrame createEventFrame, JTextField sportField,
                                             JTextField playersField, JTextField locationField) {
        JButton createEventButton = createButton("Create Event");
        createEventButton.addActionListener(e -> createEvent(adminFrame, createEventFrame, sportField, playersField,
                locationField));
        return createEventButton;
    }

    // EFFECTS: creates the back button for the create event panel
    private static JButton createEventBackButton(JFrame adminFrame, JFrame createEventFrame) {
        JButton backButton = createButton("Back to Menu");
        backButton.addActionListener(e -> {
            createEventFrame.dispose();
            adminFrame.setVisible(true);
        });
        return backButton;
    }

    // EFFECTS: creates the title for the create event window
    private static void createEventTitle(JPanel createEventPanel, GridBagConstraints gbc) {
        JLabel titleLabel = new JLabel("Create Event");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        createEventPanel.add(titleLabel, gbc);
    }

    // EFFECTS: creates the layout for the create event panel
    private static void createEventLayout(JPanel createEventPanel, GridBagConstraints gbc, JLabel sportLabel,
                                          JTextField sportField, JLabel playersLabel, JTextField playersField,
                                          JLabel locationLabel, JTextField locationField, JButton createEventButton,
                                          JButton backButton) {
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        createEventPanel.add(sportLabel, gbc);

        gbc.gridx = 1;
        createEventPanel.add(sportField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        createEventPanel.add(playersLabel, gbc);

        gbc.gridx = 1;
        createEventPanel.add(playersField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        createEventPanel.add(locationLabel, gbc);

        gbc.gridx = 1;
        createEventPanel.add(locationField, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        createEventPanel.add(createEventButton, gbc);

        gbc.gridy = 5;
        createEventPanel.add(backButton, gbc);
    }

    // EFFECTS: creates a label for the project
    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(textColour);
        return label;
    }

    // EFFECTS: creates a text field for the project
    private static JTextField createTextField() {
        return new JTextField(15);
    }

    // EFFECTS: creates an event with given details
    private static void createEvent(JFrame adminFrame, JFrame createEventFrame, JTextField sportField,
                                    JTextField playersField, JTextField locationField) {
        if (administrator.getEvents().size() > 0) {
            int eventNum = administrator.getEvents().get(administrator.getEvents().size() - 1).getEventID();
            eventID = eventNum + 1;
        }
        String sport = sportField.getText();
        int requiredPlayers = Integer.parseInt(playersField.getText());
        String location = locationField.getText();
        Event event = new Event(eventID, sport, requiredPlayers, location);
        if (administrator.createEvent(event)) {
            eventID++;
            JOptionPane.showMessageDialog(null, "Event successfully created!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        createEventFrame.dispose();
        adminFrame.setVisible(true);
    }

    // EFFECTS: checks if the event number the user inputs is valid
    private static void openModifyEventPanel(JFrame adminFrame) {
        JFrame modifyEventFrame = new JFrame("Modify Event");
        modifyEventFrame.setSize(400, 200);
        modifyEventFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        modifyEventFrame.setLocationRelativeTo(null);

        JPanel modifyEventPanel = new JPanel(new GridBagLayout());
        modifyEventPanel.setBackground(new Color(34, 45, 77));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel eventIdLabel = createLabel("Enter Event ID:");
        JTextField eventIdField = createTextField();

        JButton findEventButton = createButton("Find Event");
        findEventButton.addActionListener(e -> modifyEventInput(modifyEventFrame, eventIdField));

        JButton backButton = createButton("Cancel");
        backButton.addActionListener(e -> {
            modifyEventFrame.dispose();
            adminFrame.setVisible(true);
        });

        modifyEventLayout(modifyEventPanel, gbc, eventIdLabel, eventIdField, findEventButton, backButton);

        modifyEventFrame.setContentPane(modifyEventPanel);
        modifyEventFrame.setVisible(true);
    }

    // EFFECTS: creates the layout for the modify event panel
    private static void modifyEventLayout(JPanel modifyEventPanel, GridBagConstraints gbc, JLabel eventIdLabel,
                                          JTextField eventIdField, JButton findEventButton, JButton backButton) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        modifyEventPanel.add(eventIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        modifyEventPanel.add(eventIdField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        modifyEventPanel.add(findEventButton, gbc);

        gbc.gridx = 2;
        modifyEventPanel.add(backButton, gbc);
    }

    // EFFECTS: creates the layout for the modify event panel
    private static void modifyEventLayout(JPanel modifyEventDetailsPanel, GridBagConstraints gbc, JLabel sportLabel,
                                          JTextField sportField, JLabel playersLabel, JTextField playersField,
                                          JLabel locationLabel, JTextField locationField, JButton modifyEventButton,
                                          JButton backButton) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        modifyEventDetailsPanel.add(sportLabel, gbc);

        gbc.gridx = 1;
        modifyEventDetailsPanel.add(sportField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        modifyEventDetailsPanel.add(playersLabel, gbc);

        gbc.gridx = 1;
        modifyEventDetailsPanel.add(playersField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        modifyEventDetailsPanel.add(locationLabel, gbc);

        gbc.gridx = 1;
        modifyEventDetailsPanel.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        modifyEventDetailsPanel.add(modifyEventButton, gbc);

        gbc.gridy = 4;
        modifyEventDetailsPanel.add(backButton, gbc);
    }

    // EFFECTS: check if event ID is valid for modifyEvent
    private static void modifyEventInput(JFrame modifyEventFrame, JTextField eventIdField) {
        int eventId = Integer.parseInt(eventIdField.getText());
        Event event = administrator.findEventById(eventId);

        if (event != null) {
            openModifyEventDetailsPanel(modifyEventFrame, event);
        } else {
            JOptionPane.showMessageDialog(null, "Event not found.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: the panel where modifying an event takes place
    private static void openModifyEventDetailsPanel(JFrame adminFrame, Event event) {
        JPanel modifyEventDetailsPanel = createModifyEventPanel(adminFrame);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel sportLabel = createLabel("New Sport:");
        JTextField sportField = createTextField();
        sportField.setText(event.getSport());

        JLabel playersLabel = createLabel("New Required Players:");
        JTextField playersField = createTextField();
        playersField.setText(Integer.toString(event.getRequiredPlayers()));

        JLabel locationLabel = createLabel("New Location:");
        JTextField locationField = createTextField();
        locationField.setText(event.getLocation());

        JButton modifyEventButton = createButton("Modify Event");
        modifyEventButton.addActionListener(e -> {
            modifyEvent(event, sportField, playersField, locationField);
            adminFrame.dispose();
        });

        JButton backButton = createButton("Cancel");
        backButton.addActionListener(e -> adminFrame.dispose());

        modifyEventLayout(modifyEventDetailsPanel, gbc, sportLabel, sportField, playersLabel, playersField,
                locationLabel, locationField, modifyEventButton, backButton);

        adminFrame.setContentPane(modifyEventDetailsPanel);
        adminFrame.setVisible(true);
    }

    // EFFECTS: creates the panel for the modify event panel
    private static JPanel createModifyEventPanel(JFrame adminFrame) {
        JPanel modifyEventDetailsPanel = new JPanel(new GridBagLayout());
        modifyEventDetailsPanel.setBackground(new Color(34, 45, 77));
        adminFrame.setSize(500, 400);
        adminFrame.setLocationRelativeTo(null);
        return modifyEventDetailsPanel;
    }

    // EFFECTS: modifies event given new details
    private static void modifyEvent(Event event, JTextField sportField, JTextField playersField,
                                    JTextField locationField) {
        String sport = sportField.getText();
        String playersInput = playersField.getText();
        String location = locationField.getText();

        administrator.modifyEvent(event, sport, playersInput, location);
        JOptionPane.showMessageDialog(null, "Event modified successfully.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: the panel where viewing events takes place
    private static void openViewEventsPanel(JFrame adminFrame) {
        JFrame viewEventsFrame = new JFrame("View Events");
        viewEventsFrame.setSize(600, 400);
        viewEventsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewEventsFrame.setLocationRelativeTo(null);

        JPanel viewEventsPanel = new JPanel(new BorderLayout());
        viewEventsPanel.setBackground(new Color(34, 45, 77));

        JTextArea eventsTextArea = new JTextArea();
        eventsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(eventsTextArea);

        adminViewEvents(eventsTextArea);

        JButton backButton = createButton("Back to Menu");
        backButton.addActionListener(e -> {
            viewEventsFrame.dispose();
            adminFrame.setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(34, 45, 77));
        buttonPanel.add(backButton);

        viewEventsPanel.add(scrollPane, BorderLayout.CENTER);
        viewEventsPanel.add(buttonPanel, BorderLayout.SOUTH);

        viewEventsFrame.setContentPane(viewEventsPanel);
        viewEventsFrame.setVisible(true);
    }

    // EFFECTS: display events on text area for administrator
    private static void adminViewEvents(JTextArea eventsTextArea) {
        if (administrator.getEvents().isEmpty()) {
            eventsTextArea.append("\nThere are no events.\n");
        } else {
            for (Event event : administrator.getEvents()) {
                eventsTextArea.append(event.toString() + "\n");
            }
        }
    }

    // EFFECTS: the panel where players can view their registered events
    private static void openPlayerViewEventsPanel(Player player, JFrame playerMenuFrame) {
        JFrame viewEventsFrame = new JFrame("View Registered Events");
        viewEventsFrame.setSize(600, 400);
        viewEventsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewEventsFrame.setLocationRelativeTo(null);

        JPanel viewEventsPanel = new JPanel(new BorderLayout());
        viewEventsPanel.setBackground(new Color(34, 45, 77));

        JTextArea eventsTextArea = new JTextArea();
        eventsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(eventsTextArea);

        playerViewEvents(player, eventsTextArea);

        JButton backButton = createButton("Back to Menu");
        backButton.addActionListener(e -> {
            viewEventsFrame.dispose();
            playerMenuFrame.setVisible(true);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(34, 45, 77));
        buttonPanel.add(backButton);

        viewEventsPanel.add(scrollPane, BorderLayout.CENTER);
        viewEventsPanel.add(buttonPanel, BorderLayout.SOUTH);

        viewEventsFrame.setContentPane(viewEventsPanel);
        viewEventsFrame.setVisible(true);
    }

    // EFFECTS: display events on text area for player
    private static void playerViewEvents(Player pl, JTextArea eventsTextArea) {
        // work with the player name and not the player and set the name to the player
        if (pl.getRegisteredEvents().size() > 0) {
            for (Event event : pl.getRegisteredEvents()) {
                eventsTextArea.append(event.toString() + "\n");
            }
        } else {
            eventsTextArea.append("\nYou are not registered in any events.\n");
        }
    }

    // EFFECTS: asks the user to enter their name
    private static void openPlayerLoginPanel(JFrame mainMenuFrame) {
        JFrame playerLoginFrame = new JFrame("Player Login");
        playerLoginFrame.setSize(400, 300);
        playerLoginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playerLoginFrame.setLocationRelativeTo(null);

        JPanel playerLoginPanel = new JPanel(new GridBagLayout());
        playerLoginPanel.setBackground(new Color(34, 45, 77));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = createLabel("Enter your name:");
        JTextField nameField = createTextField();

        JButton loginButton = loginButtonAction(mainMenuFrame, playerLoginFrame, nameField);

        JButton backButton = createButton("Back to Main Menu");
        backButton.addActionListener(e -> {
            playerLoginFrame.dispose();
            mainMenuFrame.setVisible(true);
        });

        playerLoginLayout(playerLoginPanel, gbc, nameLabel, nameField, loginButton, backButton);

        playerLoginFrame.setContentPane(playerLoginPanel);
        playerLoginFrame.setVisible(true);
    }

    // EFFECTS: creates the player login layout
    private static void playerLoginLayout(JPanel playerLoginPanel, GridBagConstraints gbc, JLabel nameLabel,
                                          JTextField nameField, JButton loginButton, JButton backButton) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        playerLoginPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        playerLoginPanel.add(nameField, gbc);

        gbc.gridy = 2;
        playerLoginPanel.add(loginButton, gbc);

        gbc.gridy = 3;
        playerLoginPanel.add(backButton, gbc);
    }

    // EFFECTS: checks if the player has inputted a valid name
    // and corresponds it to a previous player object if they are not new players
    private static JButton loginButtonAction(JFrame mainMenuFrame, JFrame playerLoginFrame, JTextField nameField) {
        JButton loginButton = createButton("Login");
        loginButton.addActionListener(e -> {
            String playerName = nameField.getText();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You must enter your name.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Player player = checkPlayer(administrator, playerName);

                if (player == null) {
                    player = new Player(playerName);
                }
                openPlayerMenuPanel(player, playerLoginFrame);
                playerLoginFrame.dispose();
                mainMenuFrame.dispose();
            }
        });
        return loginButton;
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

    // EFFECTS: the menu for a player
    private static void openPlayerMenuPanel(Player player, JFrame playerLogin) {
        JFrame playerMenuFrame = new JFrame("Player Menu");
        playerMenuFrame.setSize(400, 300);
        playerMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playerMenuFrame.setLocationRelativeTo(null);

        JPanel playerMenuPanel = new JPanel(new GridBagLayout());
        playerMenuPanel.setBackground(new Color(34, 45, 77));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel playerNameLabel = createLabel("Player: " + player.getName());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        playerMenuPanel.add(playerNameLabel, gbc);

        playerMenuRegisterButton(player, playerMenuPanel, gbc);

        playerMenuOptOutButton(player, playerMenuPanel, gbc);

        playerMenuViewEventsButton(player, playerMenuFrame, playerMenuPanel, gbc);

        playerMenuSaveButton(playerMenuPanel, gbc);

        playerMenuBackButton(playerLogin, playerMenuFrame, playerMenuPanel, gbc);

        playerMenuFrame.setContentPane(playerMenuPanel);
        playerMenuFrame.setVisible(true);
    }

    // EFFECTS: creates the back button for the player menu
    private static void playerMenuBackButton(JFrame playerLogin, JFrame playerMenuFrame, JPanel playerMenuPanel,
                                             GridBagConstraints gbc) {
        JButton backButton = createButton("Back to Player Login");
        backButton.addActionListener(e -> {
            playerMenuFrame.dispose();
            playerLogin.setVisible(true);
        });
        backButton.setPreferredSize(new Dimension(360, 40));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        playerMenuPanel.add(backButton, gbc);
    }

    // EFFECTS: creates the save button for the player menu
    private static void playerMenuSaveButton(JPanel playerMenuPanel, GridBagConstraints gbc) {
        JButton saveEventsButton = createButton("Save events");
        saveEventsButton.addActionListener(e -> saveEvents());
        saveEventsButton.setPreferredSize(new Dimension(180, 40));
        gbc.gridx = 1;
        playerMenuPanel.add(saveEventsButton, gbc);
    }

    // EFFECTS: creates the view event button for the player menu
    private static void playerMenuViewEventsButton(Player player, JFrame playerMenuFrame, JPanel playerMenuPanel,
                                                   GridBagConstraints gbc) {
        JButton viewEventsButton = createButton("View registered events");
        viewEventsButton.addActionListener(e -> openPlayerViewEventsPanel(player, playerMenuFrame));
        viewEventsButton.setPreferredSize(new Dimension(180, 40));
        gbc.gridy = 2;
        gbc.gridx = 0;
        playerMenuPanel.add(viewEventsButton, gbc);
    }

    // EFFECTS: creates the opt-out button for the player menu
    private static void playerMenuOptOutButton(Player player, JPanel playerMenuPanel, GridBagConstraints gbc) {
        JButton optOutButton = createButton("Opt out of an event");
        optOutButton.addActionListener(e -> openOptOutOfEventPanel(player));
        optOutButton.setPreferredSize(new Dimension(180, 40));
        gbc.gridx = 1;
        playerMenuPanel.add(optOutButton, gbc);
    }

    // EFFECTS: creates the register button for the player menu
    private static void playerMenuRegisterButton(Player player, JPanel playerMenuPanel, GridBagConstraints gbc) {
        JButton registerButton = createButton("Register for an event");
        registerButton.addActionListener(e -> openRegisterForEventPanel(player));
        registerButton.setPreferredSize(new Dimension(180, 40));
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        playerMenuPanel.add(registerButton, gbc);
    }

    // MODIFIES: this
    // EFFECTS: the panel where registering for an event takes place
    private static void openRegisterForEventPanel(Player player) {
        JFrame registerFrame = new JFrame("Register for Event");
        registerFrame.setSize(400, 200);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setLocationRelativeTo(null); // Center on screen

        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(3, 2, 10, 10));
        registerPanel.setBackground(new Color(34, 45, 77));

        JLabel eventIdLabel = new JLabel("Enter Event ID:");
        eventIdLabel.setForeground(textColour);
        JTextField eventIdField = new JTextField();

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerPlayerForEvent(player, registerFrame, eventIdField));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> registerFrame.dispose());

        registerPanel.add(eventIdLabel);
        registerPanel.add(eventIdField);
        registerPanel.add(new JLabel());
        registerPanel.add(new JLabel());
        registerPanel.add(registerButton);
        registerPanel.add(cancelButton);

        registerFrame.setContentPane(registerPanel);
        registerFrame.setVisible(true);
    }

    // EFFECTS: register the player to the event
    private static void registerPlayerForEvent(Player player, JFrame registerFrame, JTextField eventIdField) {
        try {
            int eventID = Integer.parseInt(eventIdField.getText());
            int returnVal = player.registerForEvent(administrator, eventID, player);

            if (returnVal == 1) {
                JOptionPane.showMessageDialog(null,
                        "Registered for the event successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (returnVal == 2) {
                JOptionPane.showMessageDialog(null, "Event not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "You are already registered in the event.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            registerFrame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Invalid Event ID. Please enter a valid number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: the panel where opting out of an event takes place
    private static void openOptOutOfEventPanel(Player player) {
        JFrame optOutFrame = new JFrame("Opt Out of Event");
        optOutFrame.setSize(400, 200);
        optOutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optOutFrame.setLocationRelativeTo(null); // Center on screen

        JPanel optOutPanel = new JPanel();
        optOutPanel.setLayout(new GridLayout(3, 2, 10, 10));
        optOutPanel.setBackground(new Color(34, 45, 77));

        JLabel eventIdLabel = new JLabel("Enter Event ID:");
        eventIdLabel.setForeground(textColour);
        JTextField eventIdField = new JTextField();

        JButton optOutButton = new JButton("Opt Out");
        optOutButton.addActionListener(e -> optOutEventForPLayer(player, optOutFrame, eventIdField));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> optOutFrame.dispose());

        optOutPanel.add(eventIdLabel);
        optOutPanel.add(eventIdField);
        optOutPanel.add(new JLabel());
        optOutPanel.add(new JLabel());
        optOutPanel.add(optOutButton);
        optOutPanel.add(cancelButton);

        optOutFrame.setContentPane(optOutPanel);
        optOutFrame.setVisible(true);
    }

    // EFFECTS: opt the player out of the event
    private static void optOutEventForPLayer(Player player, JFrame optOutFrame, JTextField eventIdField) {
        try {
            int eventID = Integer.parseInt(eventIdField.getText());
            int returnVal = player.optOutOfEvent(administrator, eventID, player);

            if (returnVal == 1) {
                JOptionPane.showMessageDialog(null,
                        "Opted out of the event successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (returnVal == 2) {
                JOptionPane.showMessageDialog(null,
                        "Player not found in the event.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Event not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            optOutFrame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Invalid Event ID. Please enter a valid number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // EFFECTS: saves the workroom to file
    private static void saveEvents() {
        try {
            jsonWriter.open();
            jsonWriter.writeEvents(administrator.getEvents());
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved events successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (FileNotFoundException e) {
            System.out.println("");
        }
    }

    // EFFECTS: deletes the event
    private static void deleteEvents(JFrame adminFrame) {
        JFrame deleteFrame = createDeleteEventsFrame();

        JPanel optOutPanel = new JPanel();
        optOutPanel.setLayout(new GridLayout(3, 2, 10, 10));
        optOutPanel.setBackground(new Color(34, 45, 77));

        JLabel eventIdLabel = new JLabel("Enter Event ID:");
        eventIdLabel.setForeground(textColour);
        JTextField eventIdField = new JTextField();

        JButton optOutButton = new JButton("Delete");
        optOutButton.addActionListener(e -> deleteEvent(eventIdField));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            deleteFrame.dispose();
            adminFrame.setVisible(true);
        });

        optOutPanel.add(eventIdLabel);
        optOutPanel.add(eventIdField);
        optOutPanel.add(new JLabel());
        optOutPanel.add(new JLabel());
        optOutPanel.add(optOutButton);
        optOutPanel.add(cancelButton);

        deleteFrame.setContentPane(optOutPanel);
        deleteFrame.setVisible(true);
    }

    // EFFECTS: creates the frame for the delete events page
    private static JFrame createDeleteEventsFrame() {
        JFrame deleteFrame = new JFrame("Delete Event");
        deleteFrame.setSize(400, 200);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteFrame.setLocationRelativeTo(null); // Center on screen
        return deleteFrame;
    }

    // EFFECTS: attempt to delete an event
    private static void deleteEvent(JTextField eventIdField) {
        try {
            int eventID = Integer.parseInt(eventIdField.getText());
            int returnVal = administrator.deleteEvent(eventID, administrator);

            if (returnVal == 1) {
                JOptionPane.showMessageDialog(null,
                        "Event deleted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Event not found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Invalid Event ID. Please enter a valid number.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private static void loadEvents() {
        try {
            ArrayList<Event> loadedEvents = jsonReader.readEvents();
            administrator.setEvents(loadedEvents);
        } catch (IOException e) {
            System.out.println("");
        }
    }

    // EFFECTS: checks if user wants to load events
    private void checkLoadEvents() {
        int choice = JOptionPane.showConfirmDialog(
                null,
                "Do you want to load events?",
                "Load Events",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            loadEvents();
            JOptionPane.showMessageDialog(null, "Successfully loaded events!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // EFFECTS: prints events to console when application is closed
    public void printLog(EventLog eventLog) {
        for (EventTwo e : eventLog) {
            System.out.println(e.toString() + "\n\n");
        }
    }

    // Open the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Sports Event Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            frame.setContentPane(new MainMenuPanel(frame));
            frame.setVisible(true);
        });
    }
}


