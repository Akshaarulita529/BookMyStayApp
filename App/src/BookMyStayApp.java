/**
 * UseCase8BookingHistoryReport
 *
 * This class demonstrates how confirmed bookings are stored and used
 * for reporting and administrative review.
 *
 * Booking history is maintained in insertion order using a List.
 * Reporting is handled separately without modifying stored data.
 *
 * @author YourName
 * @version 8.0
 */

import java.util.*;

// Reservation Class (extended for history tracking)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// Booking History (Data Storage)
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Stored booking: " + reservation.getReservationId());
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n---- Booking History ----");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n---- Booking Summary Report ----");

        Map<String, Integer> roomCount = new HashMap<>();

        for (Reservation r : reservations) {
            String roomType = r.getRoomType();
            roomCount.put(roomType, roomCount.getOrDefault(roomType, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : roomCount.entrySet()) {
            System.out.println(entry.getKey() + " booked: " + entry.getValue());
        }

        System.out.println("Total bookings: " + reservations.size());
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v8.0");
        System.out.println("=======================================\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("RES-101", "Alice", "Single Room"));
        history.addReservation(new Reservation("RES-102", "Bob", "Suite Room"));
        history.addReservation(new Reservation("RES-103", "Charlie", "Single Room"));

        // Initialize reporting service
        BookingReportService reportService = new BookingReportService();

        // Display all bookings
        List<Reservation> allBookings = history.getAllReservations();
        reportService.displayAllBookings(allBookings);

        // Generate summary report
        reportService.generateSummary(allBookings);

        System.out.println("\nReporting completed without modifying booking history.");
    }
}