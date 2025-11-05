package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Room;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface AccommodationBookingService {

    // Create
    AccommodationBooking createBooking(AccommodationBooking booking);

    // Read
    List<AccommodationBooking> getAllBookings();
    Optional<AccommodationBooking> getBookingById(String id);
    List<AccommodationBooking> getBookingsByStatus(int status);
    List<AccommodationBooking> getBookingsByCustomer(UUID customerId);
    List<AccommodationBooking> searchBookings(String searchTerm, Integer status);
    long countAllBookings();

    // Update
    AccommodationBooking updateBookingDetails(AccommodationBooking booking);
    AccommodationBooking updateBookingStatus(String bookingId, int newStatus);

    // Status Management
    AccommodationBooking payBooking(String bookingId);
    AccommodationBooking cancelBooking(String bookingId);
    AccommodationBooking refundBooking(String bookingId);
    void processCheckInUpdates(); // Auto-update status when check-in date arrives

    // Business Logic
    String generateBookingId(String roomId, LocalDateTime bookingTime);
    int calculateTotalDays(LocalDateTime checkIn, LocalDateTime checkOut);
    int calculateTotalPrice(Room room, int totalDays, boolean isBreakfast);
    boolean hasConflictingBooking(String roomId, LocalDateTime checkIn, LocalDateTime checkOut);
    boolean canUpdateBooking(AccommodationBooking booking);
    boolean canCancelBooking(AccommodationBooking booking);

    // Price Calculation
    int calculatePriceChange(AccommodationBooking oldBooking, AccommodationBooking newBooking);
    void handlePriceIncrease(AccommodationBooking booking, int priceDifference);
    void handlePriceDecrease(AccommodationBooking booking, int priceDifference);

    // Property Income Management
    void updatePropertyIncome(String propertyId, int amount, boolean isIncrease);

    // Statistics
    Map<String, Integer> getBookingStatistics(int month, int year);
    List<AccommodationBooking> getDoneBookingsByMonth(int month, int year);
}
