package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomService {

    // Create
    Room createRoom(Room room);
    List<Room> createMultipleRooms(List<Room> rooms);

    // Read
    List<Room> getAllRooms();
    Optional<Room> getRoomById(String id);
    List<Room> getRoomsByRoomType(RoomType roomType);
    List<Room> getRoomsByPropertyId(String propertyId);
    List<Room> getAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut);
    long countRoomsByPropertyAndFloor(String propertyId, int floor);

    // Update
    Room updateRoom(Room room);
    Room updateRoomMaintenance(String roomId, LocalDateTime maintenanceStart, LocalDateTime maintenanceEnd);

    // Business Logic
    String generateRoomId(String propertyId, int unitNumber);
    boolean isRoomAvailable(String roomId, LocalDateTime checkIn, LocalDateTime checkOut);
    void updateRoomAvailability(String roomId, int availabilityStatus);
    List<Room> getRoomsInMaintenance();
}
