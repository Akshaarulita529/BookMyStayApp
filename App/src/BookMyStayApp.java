/**
 * UseCase6RoomAllocationService
 *
 * This class demonstrates reservation confirmation and safe room allocation.
 * It ensures FIFO processing, prevents double-booking, and keeps inventory consistent.
 *
 * @author YourName
 * @version 6.0
 */

import java.util.*;

// Reservation Class
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void reduceAvailability(String roomType) {
        inventory.put(roomType, getAvailability(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Updated Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Request Queue
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Booking Service (Core Logic)
class BookingService {

    private RoomInventory inventory;

    // Map roomType -> Set of allocated room IDs
    private Map<String, Set<String>> allocatedRooms;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
    }

    // Process booking queue
    public void processBookings(BookingRequestQueue queue) {

        System.out.println("---- Processing Bookings ----\n");

        while (!queue.isEmpty()) {
            Reservation request = queue.getNextRequest();

            String roomType = request.getRoomType();
            String guest = request.getGuestName();

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure Set exists
                allocatedRooms.putIfAbsent(roomType, new HashSet<>());

                // Add to allocated set (ensures uniqueness)
                allocatedRooms.get(roomType).add(roomId);

                // Update inventory immediately
                inventory.reduceAvailability(roomType);

                // Confirm reservation
                System.out.println("Booking CONFIRMED for " + guest +
                        " | Room Type: " + roomType +
                        " | Room ID: " + roomId);

            } else {
                // No rooms available
                System.out.println("Booking FAILED for " + guest +
                        " | Room Type: " + roomType +
                        " (No availability)");
            }
        }
    }

    public void displayAllocatedRooms() {
        System.out.println("\n---- Allocated Rooms ----");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main Class
public class BookMyStayApp{

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v6.0");
        System.out.println("=======================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // exceeds availability
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Process bookings
        BookingService service = new BookingService(inventory);
        service.processBookings(queue);

        // Display results
        service.displayAllocatedRooms();
        inventory.displayInventory();

        System.out.println("\nAll bookings processed with consistency and no double-booking.");
    }
}