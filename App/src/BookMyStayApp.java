/**
 * UseCase11ConcurrentBookingSimulation
 *
 * Demonstrates thread-safe handling of concurrent hotel bookings.
 * Multiple guests submit booking requests simultaneously.
 * Synchronization prevents double-booking and maintains inventory consistency.
 *
 * @author YourName
 * @version 11.0
 */

import java.util.*;
import java.util.concurrent.*;

// Reservation class
class Reservation {
    private String guestName;
    private String roomType;
    private String roomId;

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

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}

// Thread-safe inventory class
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        allocatedRooms.put("Single Room", new HashSet<>());
        allocatedRooms.put("Double Room", new HashSet<>());
    }

    // Thread-safe allocation
    public synchronized boolean allocateRoom(Reservation reservation) {
        String type = reservation.getRoomType();
        int available = inventory.getOrDefault(type, 0);

        if (available <= 0) {
            return false; // No rooms available
        }

        // Allocate room ID
        String roomId = type.substring(0, 2).toUpperCase() + "-" + (available + allocatedRooms.get(type).size());
        reservation.setRoomId(roomId);

        // Update inventory and allocated rooms
        inventory.put(type, available - 1);
        allocatedRooms.get(type).add(roomId);
        return true;
    }

    public synchronized void displayInventory() {
        System.out.println("\n---- Inventory Status ----");
        for (String type : inventory.keySet()) {
            System.out.println(type + " available: " + inventory.get(type) + ", allocated IDs: " + allocatedRooms.get(type));
        }
    }
}

// Booking service running in a thread
class BookingTask implements Runnable {
    private Reservation reservation;
    private RoomInventory inventory;

    public BookingTask(Reservation reservation, RoomInventory inventory) {
        this.reservation = reservation;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        boolean success = inventory.allocateRoom(reservation);

        if (success) {
            System.out.println("Booking SUCCESS: " + reservation.getGuestName() +
                    " | Room: " + reservation.getRoomType() +
                    " | Room ID: " + reservation.getRoomId());
        } else {
            System.out.println("Booking FAILED (No availability): " + reservation.getGuestName() +
                    " | Room: " + reservation.getRoomType());
        }
    }
}

// Main class
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Concurrent Booking Simulation v11.0");
        System.out.println("=======================================\n");

        RoomInventory inventory = new RoomInventory();

        // Simulate multiple guests submitting requests concurrently
        List<Reservation> reservations = Arrays.asList(
                new Reservation("Alice", "Single Room"),
                new Reservation("Bob", "Single Room"),
                new Reservation("Charlie", "Double Room"),
                new Reservation("Diana", "Single Room") // Will exceed availability
        );

        // Use ExecutorService to run tasks concurrently
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (Reservation r : reservations) {
            executor.submit(new BookingTask(r, inventory));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        inventory.displayInventory();

        System.out.println("\nAll booking requests processed safely under concurrency.");
    }
}