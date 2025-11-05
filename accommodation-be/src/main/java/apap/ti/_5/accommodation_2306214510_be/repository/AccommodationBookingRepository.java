package apap.ti._5.accommodation_2306214510_be.repository;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AccommodationBookingRepository extends JpaRepository<AccommodationBooking, String> {

    // Find all bookings ordered by booking ID
    List<AccommodationBooking> findAllByOrderByBookingID();

    // Find bookings by status
    List<AccommodationBooking> findByStatusOrderByBookingID(int status);

    // Find bookings by customer ID
    List<AccommodationBooking> findByCustomerIDOrderByBookingID(UUID customerID);

    // Find bookings by room
    List<AccommodationBooking> findByRoom(Room room);

    // Find bookings by room ID
    List<AccommodationBooking> findByRoomRoomIDOrderByBookingID(String roomID);

    // Check for conflicting bookings (same room, overlapping dates, not cancelled/done)
    @Query("SELECT b FROM AccommodationBooking b WHERE b.room.roomID = :roomID AND " +
           "b.status NOT IN (2, 4) AND " +
           "(:checkIn < b.checkOutDate AND :checkOut > b.checkInDate)")
    List<AccommodationBooking> findConflictingBookings(@Param("roomID") String roomID,
                                                       @Param("checkIn") LocalDateTime checkIn,
                                                       @Param("checkOut") LocalDateTime checkOut);

    // Find bookings that need to be marked as done (check-in date has passed and status is 1)
    @Query("SELECT b FROM AccommodationBooking b WHERE b.checkInDate <= :currentDate AND b.status = 1")
    List<AccommodationBooking> findBookingsToMarkAsDone(@Param("currentDate") LocalDateTime currentDate);

    // Find bookings that need to be cancelled (check-in date has passed and status is 0 or 3)
    @Query("SELECT b FROM AccommodationBooking b WHERE b.checkInDate <= :currentDate AND b.status IN (0, 3)")
    List<AccommodationBooking> findBookingsToCancel(@Param("currentDate") LocalDateTime currentDate);

    // Search bookings by property name or room name
    @Query("SELECT b FROM AccommodationBooking b WHERE " +
           "LOWER(b.room.roomType.property.propertyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.room.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<AccommodationBooking> searchBookings(@Param("searchTerm") String searchTerm);

    // Search bookings by property name or room name and status
    @Query("SELECT b FROM AccommodationBooking b WHERE " +
           "(LOWER(b.room.roomType.property.propertyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.room.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "b.status = :status")
    List<AccommodationBooking> searchBookingsByStatus(@Param("searchTerm") String searchTerm, @Param("status") int status);

    // Find bookings by property ID and status 4 (Done) for statistics
    @Query("SELECT b FROM AccommodationBooking b WHERE " +
           "b.room.roomType.property.propertyID = :propertyID AND " +
           "b.status = 4 AND " +
           "MONTH(b.checkInDate) = :month AND YEAR(b.checkInDate) = :year")
    List<AccommodationBooking> findDoneBookingsByPropertyAndMonth(@Param("propertyID") String propertyID,
                                                                   @Param("month") int month,
                                                                   @Param("year") int year);

    // Count total bookings
    long count();

    // Count bookings by status
    long countByStatus(int status);

    // Find all bookings for a specific property
    @Query("SELECT b FROM AccommodationBooking b WHERE b.room.roomType.property.propertyID = :propertyID")
    List<AccommodationBooking> findByPropertyID(@Param("propertyID") String propertyID);

    // Find bookings after a certain date for a property
    @Query("SELECT b FROM AccommodationBooking b WHERE b.room.roomType.property.propertyID = :propertyID AND b.checkInDate >= :date")
    List<AccommodationBooking> findByPropertyIDAndCheckInDateAfter(@Param("propertyID") String propertyID, @Param("date") LocalDateTime date);
}
