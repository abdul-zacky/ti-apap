package apap.ti._5.accommodation_2306214510_be.repository;

import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    // Find rooms by room type
    List<Room> findByRoomType(RoomType roomType);

    // Find rooms by room type ID
    List<Room> findByRoomTypeRoomTypeID(String roomTypeID);

    // Find available rooms by room type
    List<Room> findByRoomTypeAndAvailabilityStatusAndActiveRoom(RoomType roomType, int availabilityStatus, int activeRoom);

    // Find rooms by property ID through room type
    @Query("SELECT r FROM Room r WHERE r.roomType.property.propertyID = :propertyID")
    List<Room> findByPropertyID(@Param("propertyID") String propertyID);

    // Check room availability for booking dates
    @Query("SELECT r FROM Room r WHERE r.roomID = :roomID AND " +
           "(r.maintenanceStart IS NULL OR r.maintenanceEnd IS NULL OR " +
           "NOT (:checkIn < r.maintenanceEnd AND :checkOut > r.maintenanceStart))")
    Optional<Room> findAvailableRoom(@Param("roomID") String roomID,
                                     @Param("checkIn") LocalDateTime checkIn,
                                     @Param("checkOut") LocalDateTime checkOut);

    // Find rooms with active maintenance
    @Query("SELECT r FROM Room r WHERE r.maintenanceStart IS NOT NULL AND r.maintenanceEnd IS NOT NULL AND " +
           "r.maintenanceStart <= :currentDate AND r.maintenanceEnd >= :currentDate")
    List<Room> findRoomsInMaintenance(@Param("currentDate") LocalDateTime currentDate);

    // Count active rooms by room type
    long countByRoomTypeAndActiveRoom(RoomType roomType, int activeRoom);
}
