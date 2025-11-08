package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final AccommodationBookingRepository bookingRepository;

    // Constructor Dependency Injection
    public RoomServiceImpl(RoomRepository roomRepository,
                           AccommodationBookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Room createRoom(Room room) {
        room.setCreatedDate(LocalDateTime.now());
        room.setUpdatedDate(LocalDateTime.now());
        room.setAvailabilityStatus(1); // Available by default
        room.setActiveRoom(1); // Active by default
        return roomRepository.save(room);
    }

    @Override
    public List<Room> createMultipleRooms(List<Room> rooms) {
        List<Room> savedRooms = new ArrayList<>();
        for (Room room : rooms) {
            room.setCreatedDate(LocalDateTime.now());
            room.setUpdatedDate(LocalDateTime.now());
            room.setAvailabilityStatus(1);
            room.setActiveRoom(1);
            savedRooms.add(roomRepository.save(room));
        }
        return savedRooms;
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<Room> getRoomById(String id) {
        return roomRepository.findById(id);
    }

    @Override
    public List<Room> getRoomsByRoomType(RoomType roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    @Override
    public List<Room> getRoomsByPropertyId(String propertyId) {
        return roomRepository.findByPropertyID(propertyId);
    }

    @Override
    public List<Room> getAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut) {
        List<Room> allRooms = roomRepository.findAll();
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            if (isRoomAvailable(room.getRoomID(), checkIn, checkOut)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    @Override
    public long countRoomsByPropertyAndFloor(String propertyId, int floor) {
        // Get all rooms for property
        List<Room> roomsForProperty = roomRepository.findByPropertyID(propertyId);
        
        // Count rooms on this floor
        // A room is on floor X if its room number starts with floor*100
        return roomsForProperty.stream()
                .filter(room -> {
                    try {
                        // Extract floor from room name (which is the room number)
                        int roomNumber = Integer.parseInt(room.getName());
                        return roomNumber >= (floor * 100) && roomNumber < ((floor + 1) * 100);
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .count();
    }

    @Override
    public Room updateRoom(Room room) {
        room.setUpdatedDate(LocalDateTime.now());
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoomMaintenance(String roomId, LocalDateTime maintenanceStart, LocalDateTime maintenanceEnd) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setMaintenanceStart(maintenanceStart);
            room.setMaintenanceEnd(maintenanceEnd);
            room.setAvailabilityStatus(0); // Unavailable during maintenance
            room.setUpdatedDate(LocalDateTime.now());
            return roomRepository.save(room);
        }
        return null;
    }

    @Override
    public String generateRoomId(String propertyId, int unitNumber) {
        return propertyId + "-" + String.format("%d", unitNumber);
    }

    @Override
    public boolean isRoomAvailable(String roomId, LocalDateTime checkIn, LocalDateTime checkOut) {
        log.debug("Checking availability for room: {} from {} to {}", roomId, checkIn, checkOut);
        
        // Check maintenance schedule
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
            log.warn("Room not found: {}", roomId);
            return false;
        }

        Room room = roomOpt.get();

        // Check if room is in maintenance during the requested period
        if (room.getMaintenanceStart() != null && room.getMaintenanceEnd() != null) {
            if (checkIn.isBefore(room.getMaintenanceEnd()) && checkOut.isAfter(room.getMaintenanceStart())) {
                log.info("Room {} in maintenance: {} to {}", roomId, room.getMaintenanceStart(), room.getMaintenanceEnd());
                return false;
            }
        }

        // Check for conflicting bookings - try both JPQL and native query
        var conflictingBookings = bookingRepository.findConflictingBookings(roomId, checkIn, checkOut);
        log.debug("JPQL Conflicting bookings for room {}: {}", roomId, conflictingBookings.size());
        
        // If JPQL returns empty but we suspect an issue, also try native query
        if (conflictingBookings.isEmpty()) {
            var nativeConflicts = bookingRepository.findConflictingBookingsNative(roomId, checkIn, checkOut);
            log.debug("Native Conflicting bookings for room {}: {}", roomId, nativeConflicts.size());
            if (!nativeConflicts.isEmpty()) {
                log.info("Native query found {} conflicts for room {}", nativeConflicts.size(), roomId);
            }
            return nativeConflicts.isEmpty();
        }
        
        log.info("Room {} has {} conflicting bookings", roomId, conflictingBookings.size());
        conflictingBookings.forEach(b -> log.info("  - Booking: {} Status: {} CheckIn: {} CheckOut: {}", 
            b.getBookingID(), b.getStatus(), b.getCheckInDate(), b.getCheckOutDate()));
        
        return conflictingBookings.isEmpty();
    }

    @Override
    public void updateRoomAvailability(String roomId, int availabilityStatus) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            room.setAvailabilityStatus(availabilityStatus);
            room.setUpdatedDate(LocalDateTime.now());
            roomRepository.save(room);
        }
    }

    @Override
    public List<Room> getRoomsInMaintenance() {
        return roomRepository.findRoomsInMaintenance(LocalDateTime.now());
    }
}
