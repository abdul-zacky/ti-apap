package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.repository.RoomRepository;
import apap.ti._5.accommodation_2306214510_be.dto.BaseResponseDTO;
import apap.ti._5.accommodation_2306214510_be.dto.RoomDTO;
import apap.ti._5.accommodation_2306214510_be.dto.RoomTypeSummaryDTO;
import apap.ti._5.accommodation_2306214510_be.dto.RoomDetailDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.MaintenanceRequestDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.RoomTypeRequestDTO;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import apap.ti._5.accommodation_2306214510_be.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/property")
@Slf4j
public class RoomRestController {

    private final PropertyService propertyService;
    private final RoomTypeService roomTypeService;
    private final RoomService roomService;
    private final RoomRepository roomRepository;

    public RoomRestController(PropertyService propertyService,
                              RoomTypeService roomTypeService,
                              RoomService roomService,
                              RoomRepository roomRepository) {
        this.propertyService = propertyService;
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    // POST add room type to existing property
    @PostMapping("/updateroom")
    public ResponseEntity<BaseResponseDTO<Property>> addRoomType(
            @RequestParam String propertyID,
            @RequestBody RoomTypeRequestDTO request) {

        try {
            Optional<Property> propertyOpt = propertyService.getPropertyById(propertyID);

            if (propertyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Property not found",
                                null
                        ));
            }

            Property property = propertyOpt.get();

            // Check for duplicate room type
            if (roomTypeService.isDuplicateRoomType(property, request.getName(), request.getFloor())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Duplicate room type: " + request.getName() + " on floor " + request.getFloor(),
                                null
                        ));
            }

            // Count properties for this owner to get propertyCount for ID generation
            long propertyCount = propertyService.countPropertiesByOwner(property.getOwnerID());

            // Create room type
            RoomType roomType = new RoomType();
            String roomTypeId = roomTypeService.generateRoomTypeId(propertyCount, request.getName(), request.getFloor());
            roomType.setRoomTypeID(roomTypeId);
            roomType.setName(request.getName());
            roomType.setPrice(request.getPrice());
            roomType.setDescription(request.getDescription());
            roomType.setCapacity(request.getCapacity());
            roomType.setFacility(request.getFacility());
            roomType.setFloor(request.getFloor());
            roomType.setProperty(property);

            RoomType savedRoomType = roomTypeService.createRoomType(roomType);

            // Get existing room count on this floor for this property
            int existingRoomsOnFloor = (int) roomService.countRoomsByPropertyAndFloor(propertyID, request.getFloor());
            
            // Create rooms for this room type
            for (int i = 1; i <= request.getUnit(); i++) {
                Room room = new Room();
                // Room number: floor * 100 + next available number
                int roomNumber = request.getFloor() * 100 + existingRoomsOnFloor + i;
                String roomId = roomService.generateRoomId(propertyID, roomNumber);
                room.setRoomID(roomId);
                room.setName(String.valueOf(roomNumber));
                room.setRoomType(savedRoomType);
                roomService.createRoom(room);
            }

            // Refresh property with updated room types before calculating total rooms
            Optional<Property> refreshedPropertyOpt = propertyService.getPropertyById(propertyID);
            if (refreshedPropertyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Property not found after update",
                                null
                        ));
            }

            Property refreshedProperty = refreshedPropertyOpt.get();
            // Update total rooms with refreshed data
            refreshedProperty.setTotalRoom(propertyService.calculateTotalRooms(refreshedProperty));
            Property updatedProperty = propertyService.updateProperty(refreshedProperty);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.CREATED.value(),
                            "Room type added successfully",
                            updatedProperty
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error adding room type: " + e.getMessage(),
                            null
                    ));
        }
    }

    // POST schedule room maintenance
    @PostMapping("/maintenance/add")
    public ResponseEntity<BaseResponseDTO<Room>> scheduleRoomMaintenance(
            @RequestBody MaintenanceRequestDTO request) {

        try {
            Optional<Room> roomOpt = roomService.getRoomById(request.getRoomID());

            if (roomOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Room not found",
                                null
                        ));
            }

            Room room = roomOpt.get();

            // Validate maintenance dates
            if (request.getMaintenanceEnd().isBefore(request.getMaintenanceStart())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Maintenance end date must be after start date",
                                null
                        ));
            }

            // Schedule maintenance
            room.setMaintenanceStart(request.getMaintenanceStart());
            room.setMaintenanceEnd(request.getMaintenanceEnd());
            Room updatedRoom = roomService.updateRoom(room);

            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Room maintenance scheduled successfully",
                    updatedRoom
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error scheduling maintenance: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET rooms by property ID - returns room type summary with counts
    @GetMapping("/{propertyID}/rooms")
    public ResponseEntity<BaseResponseDTO<List<RoomTypeSummaryDTO>>> getRoomsByPropertyId(
            @PathVariable String propertyID) {
        try {
            Optional<Property> propertyOpt = propertyService.getPropertyById(propertyID);

            if (propertyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Property not found",
                                null
                        ));
            }

            Property property = propertyOpt.get();
            
            // Get room types for this property
            List<RoomType> roomTypes = roomTypeService.getRoomTypesByPropertyId(propertyID);
            
            // Convert to summary DTOs with room counts
            List<RoomTypeSummaryDTO> summaries = roomTypes.stream().map(roomType -> {
                // Count total rooms of this type
                long totalRooms = roomType.getListRoom() != null ? roomType.getListRoom().size() : 0;
                
                // Count available rooms (availability_status = 1)
                long availableRooms = roomType.getListRoom() != null ? 
                    roomType.getListRoom().stream()
                        .filter(room -> room.getAvailabilityStatus() == 1)
                        .count() : 0;
                
                RoomTypeSummaryDTO dto = new RoomTypeSummaryDTO();
                dto.setRoomTypeID(roomType.getRoomTypeID());
                dto.setName(roomType.getName());
                dto.setCapacity(roomType.getCapacity());
                dto.setPrice(roomType.getPrice());
                dto.setFloor(roomType.getFloor());
                dto.setDescription(roomType.getDescription());
                dto.setFacility(roomType.getFacility());
                dto.setTotalRooms(totalRooms);
                dto.setAvailableRooms(availableRooms);
                
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Success",
                    summaries
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error fetching rooms: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET check room availability for date range
    @GetMapping("/{propertyID}/rooms/available")
    public ResponseEntity<BaseResponseDTO<List<RoomTypeSummaryDTO>>> checkRoomAvailability(
            @PathVariable String propertyID,
            @RequestParam String checkIn,
            @RequestParam String checkOut) {
        
        try {
            Optional<Property> propertyOpt = propertyService.getPropertyById(propertyID);
            
            if (propertyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Property not found",
                                null
                        ));
            }

            // Parse check-in and check-out dates
            LocalDateTime checkInDateTime = LocalDateTime.parse(checkIn);
            LocalDateTime checkOutDateTime = LocalDateTime.parse(checkOut);

            log.info("=== CHECK ROOM AVAILABILITY ===");
            log.info("Property ID: {}", propertyID);
            log.info("Check-In: {}", checkInDateTime);
            log.info("Check-Out: {}", checkOutDateTime);

            // Get all room types for property (fresh query)
            List<RoomType> roomTypes = roomTypeService.getRoomTypesByPropertyId(propertyID);
            log.info("Found {} room types", roomTypes.size());
            
            // Check availability for each room type
            List<RoomTypeSummaryDTO> summaries = roomTypes.stream().map(roomType -> {
                long totalRooms = roomType.getListRoom() != null ? roomType.getListRoom().size() : 0;
                
                // Count available rooms by querying each room individually (forces fresh load from DB)
                long availableRooms = 0;
                if (roomType.getListRoom() != null) {
                    for (Room room : roomType.getListRoom()) {
                        // Re-fetch room from DB to ensure we get latest maintenance data
                        Optional<Room> freshRoom = roomRepository.findById(room.getRoomID());
                        boolean isAvailable = freshRoom.isPresent() && roomService.isRoomAvailable(room.getRoomID(), checkInDateTime, checkOutDateTime);
                        log.info("Room: {} - Available: {}", room.getRoomID(), isAvailable);
                        if (isAvailable) {
                            availableRooms++;
                        }
                    }
                }
                
                log.info("RoomType: {} - Total: {} - Available: {}", roomType.getName(), totalRooms, availableRooms);
                
                RoomTypeSummaryDTO dto = new RoomTypeSummaryDTO();
                dto.setRoomTypeID(roomType.getRoomTypeID());
                dto.setName(roomType.getName());
                dto.setCapacity(roomType.getCapacity());
                dto.setPrice(roomType.getPrice());
                dto.setFloor(roomType.getFloor());
                dto.setDescription(roomType.getDescription());
                dto.setFacility(roomType.getFacility());
                dto.setTotalRooms(totalRooms);
                dto.setAvailableRooms(availableRooms);
                return dto;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Success",
                    summaries
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error checking availability: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET check if a single room is available for date range
    @GetMapping("/room/{roomID}/available")
    public ResponseEntity<BaseResponseDTO<Boolean>> checkSingleRoomAvailability(
            @PathVariable String roomID,
            @RequestParam String checkIn,
            @RequestParam String checkOut) {
        
        try {
            Optional<Room> roomOpt = roomRepository.findById(roomID);
            
            if (roomOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Room not found",
                                null
                        ));
            }

            // Parse check-in and check-out dates
            LocalDateTime checkInDateTime = LocalDateTime.parse(checkIn);
            LocalDateTime checkOutDateTime = LocalDateTime.parse(checkOut);

            log.info("=== CHECK SINGLE ROOM AVAILABILITY ===");
            log.info("Room ID: {}", roomID);
            log.info("Check-In: {}", checkInDateTime);
            log.info("Check-Out: {}", checkOutDateTime);

            // Check if room is available
            boolean isAvailable = roomService.isRoomAvailable(roomID, checkInDateTime, checkOutDateTime);
            
            log.info("Room {} is available: {}", roomID, isAvailable);
            
            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    isAvailable ? "Room is available" : "Room is not available",
                    isAvailable
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error checking room availability: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET all individual rooms for a property
    @GetMapping("/{propertyID}/all-rooms")
    public ResponseEntity<BaseResponseDTO<List<RoomDetailDTO>>> getAllRooms(
            @PathVariable String propertyID) {
        try {
            Optional<Property> propertyOpt = propertyService.getPropertyById(propertyID);

            if (propertyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Property not found",
                                null
                        ));
            }

            // Get all room types for property
            List<RoomType> roomTypes = roomTypeService.getRoomTypesByPropertyId(propertyID);
            
            // Collect all individual rooms
            List<RoomDetailDTO> allRooms = new java.util.ArrayList<>();
            for (RoomType roomType : roomTypes) {
                if (roomType.getListRoom() != null) {
                    for (Room room : roomType.getListRoom()) {
                        RoomDetailDTO dto = new RoomDetailDTO();
                        dto.setRoomID(room.getRoomID());
                        dto.setName(room.getName());
                        dto.setAvailabilityStatus(room.getAvailabilityStatus());
                        dto.setActiveRoom(room.getActiveRoom());
                        dto.setRoomTypeID(room.getRoomType().getRoomTypeID());
                        allRooms.add(dto);
                    }
                }
            }
            
            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Success",
                    allRooms
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error fetching all rooms: " + e.getMessage(),
                            null
                    ));
        }
    }

    // PUT sync totalRoom count for a property
    @PutMapping("/{propertyID}/sync-total-rooms")
    public ResponseEntity<BaseResponseDTO<Property>> syncTotalRooms(
            @PathVariable String propertyID) {
        try {
            Optional<Property> propertyOpt = propertyService.getPropertyById(propertyID);

            if (propertyOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Property not found",
                                null
                        ));
            }

            Property property = propertyOpt.get();
            
            // Refresh room types from database to ensure relationships are loaded
            List<RoomType> roomTypes = roomTypeService.getRoomTypesByPropertyId(propertyID);
            property.setListRoomType(roomTypes);
            
            // Recalculate total rooms
            int totalRooms = propertyService.calculateTotalRooms(property);
            property.setTotalRoom(totalRooms);
            
            // Update property in database
            Property updatedProperty = propertyService.updateProperty(property);

            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Total rooms synced successfully. New total: " + totalRooms,
                    updatedProperty
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error syncing total rooms: " + e.getMessage(),
                            null
                    ));
        }
    }
}
