# <div style="text-align: center;">Sport Scheduler</div>

This project is a sport scheduler application that aims to streamline the process of organizing and participating
in sports events. This application will cater to two primary user groups: administrators and players.

## *Scope*
Administrators, typically coaches or event organizers, will have the ability to create and manage sports events
effortlessly. They can input crucial event details such as the location (e.g., a local field or sports complex),
the required number of players (e.g., 11 for a soccer match), and the specific positions that need to be filled
for the sport (e.g., goalkeeper, defenders, forwards). Administrators can also make real-time updates to event
information, including changing the event's location, adjusting the number of required players, or even canceling
the event if necessary.

On the player side, sports enthusiasts can log in to the application and explore a dynamic list of available sports
events created by administrators. When players opt to participate in an event, they have the option to choose from
the available positions, ensuring that they are assigned roles that best match their skills and preferences. As 
players sign up for events, the application will automatically update the list, displaying their names alongside
the events they've chosen. Importantly, players can also opt out of events they initially committed to, which opens
up spots for others and allows for flexibility in participation.

This project holds a personal interest for me because I am passionate about sports, particularly soccer, and I
understand firsthand the significance of a well-organized scheduler in ensuring smooth gameplay. It can be
frustrating when playing with friends or in a team setting, and roles and positions are not clearly defined.
I have experienced the challenges of miscommunication and confusion during such instances, and I believe that a
dedicated application can alleviate these issues.

Beyond the sports community, this application has broader implications, as it can benefit not only coaches and
their teams but also friends who simply want to schedule and enjoy various sports activities together.

## *User Stories*
- As an administrator, I want to be able to create a sports event by specifying the sport name, the number of
required players, preferred positions, and the event location to the list of current events available.
- As an administrator, I want to be able to modify any of my events by changing the required information or
delete the event.
- As both an administrator and a player, I want to be able to view the list of currently available
sports events.
- As a player, I want to be able to opt into an event from the list of available sports events. Additionally,
I want to be able to have the option to choose from the available positions in the event.
- As a player, I want to be able to opt out of the events that I am participating in.
- As a player and an administrator, I want to be able to have the option to save the list of events and its 
corresponding details and players when I close the application.
- As a player and an administrator, I want to be able to have the option to reload the list of events exactly
as it appeared during my previous use of the application.


## *Instructions for Grader*

- You can generate the first required action related to adding events to the list of events by going into the 
administrator menu and creating an event through the create event button.
- You can generate the second required action related to adding players to an event by logging in as a player and 
entering into an existing event.
- You can locate my visual component by looking at the main menu.
- You can save the state of my application by clicking the save button in the administrator and player menu.
- You can reload the state of my application by clicking yes to the pop-up message when the application starts.

## *Phase 4: Task 2*

Tue Nov 28 14:09:02 PST 2023
New event created: 1


Tue Nov 28 14:09:09 PST 2023
Event name modified to: soccer


Tue Nov 28 14:09:09 PST 2023
Event required players modified to: 5


Tue Nov 28 14:09:09 PST 2023
Event location modified to: ubc


Tue Nov 28 14:09:17 PST 2023
x added to Event: 1


Tue Nov 28 14:09:21 PST 2023
Player opted out of the event: 1


Tue Nov 28 14:09:29 PST 2023
Event deleted: 1

## *Phase 4: Task 3*

A system made up of several classes connected by relationships is depicted in the UML class diagram. The diagram 
illustrates, for example, the relationship between the Event class—which stores references to Player objects—and the 
Administrator class, which is in charge of overseeing Event instances. This association means that one or more Event 
occurrences, involving different Player entities, are under the control of an Administrator.

The diagram also illustrates how Singleton patterns are used in particular classes, like JsonReader and EventLog. 
Throughout the system's operation, these patterns guarantee that there is only one instance of each of these classes. 
For instance, the JsonReader class handles a map of Player entities, but the EventLog class maintains a collection of 
EventTwo instances. 

Given more time to refine the project, I would enhance the error handling procedures of the program to provide 
user-centric notifications that are clearer would improve the overall user experience. Through the integration of unique
exception classes, the logging of detailed error information, and the display of customized alerts to users, the program
may be made more user-friendly. Situations such as insufficient event space or accidentally entering a text rather than
an intended integer, for example, could result in helpful error messages for users, improving usability and 
comprehension.