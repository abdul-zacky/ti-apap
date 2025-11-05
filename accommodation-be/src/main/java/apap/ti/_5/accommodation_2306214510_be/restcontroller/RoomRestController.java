package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.BaseResponseDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.MaintenanceRequestDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.RoomTypeRequestDTO;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import apap.ti._5.accommodation_2306214510_be.service.RoomTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/property")
@CrossOrigin(origins = "*")
public class RoomRestController {

    private final PropertyService propertyService;
    private final RoomTypeService roomTypeService;
    private final RoomService roomService;

    public RoomRestController(PropertyService propertyService,
                              RoomTypeService roomTypeService,
                              RoomService roomService) {
        this.propertyService = propertyService;
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
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

            // Create rooms for this room type
            for (int i = 1; i <= request.getUnit(); i++) {
                Room room = new Room();
                String roomId = roomService.generateRoomId(propertyID, (request.getFloor() * 100) + i);
                room.setRoomID(roomId);
                room.setName(String.valueOf((request.getFloor() * 100) + i));
                room.setRoomType(savedRoomType);
                roomService.createRoom(room);
            }

            // Update total rooms
            property.setTotalRoom(propertyService.calculateTotalRooms(property));
            Property updatedProperty = propertyService.updateProperty(property);

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
}
