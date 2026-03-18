/**
 * UseCase4RoomSearch
 *
 * This class demonstrates room search functionality with read-only access
 * to inventory. Only available rooms are displayed without modifying state.
 *
 * @author YourName
 * @version 4.0
 */

import java.util.*;

// Abstract Room Class
abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: $" + price);
    }
}

// Concrete Room Classes
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 300.0);
    }
}

// Inventory Class (Read-only usage here)
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 0); // unavailable
        inventory.put("Suite Room", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// Search Service Class
class RoomSearchService {

    public void searchAvailableRooms(List<Room> rooms, RoomInventory inventory) {
        System.out.println("---- Available Rooms ----\n");

        for (Room room : rooms) {
            int available = inventory.getAvailability(room.getRoomType());

            // Filter unavailable rooms
            if (available > 0) {
                room.displayDetails();
                System.out.println("Available: " + available + "\n");
            }
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v4.0");
        System.out.println("=======================================\n");

        // Initialize rooms (domain objects)
        List<Room> rooms = new ArrayList<>();
        rooms.add(new SingleRoom());
        rooms.add(new DoubleRoom());
        rooms.add(new SuiteRoom());

        // Initialize inventory (state)
        RoomInventory inventory = new RoomInventory();

        // Perform search (read-only)
        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(rooms, inventory);

        System.out.println("Search completed. No changes made to inventory.");
    }
}