package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.BaseResponseDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.PropertyRequestDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/property")
@CrossOrigin(origins = "*")
public class PropertyRestController {

    private final PropertyService propertyService;
    private final RoomTypeService roomTypeService;
    private final RoomService roomService;

    // Constructor Dependency Injection
    public PropertyRestController(PropertyService propertyService,
                                  RoomTypeService roomTypeService,
                                  RoomService roomService) {
        this.propertyService = propertyService;
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
    }

    // GET all properties with optional filters
    @GetMapping("")
    public ResponseEntity<BaseResponseDTO<List<Property>>> getAllProperties(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer status) {

        List<Property> properties = propertyService.searchProperties(name, type, status);
        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                properties
        ));
    }

    // GET property by ID
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<Property>> getPropertyById(@PathVariable String id) {
        Optional<Property> property = propertyService.getPropertyById(id);

        if (property.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.NOT_FOUND.value(),
                            "Property not found",
                            null
                    ));
        }

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                property.get()
        ));
    }

    // POST create property
    @PostMapping("/create")
    public ResponseEntity<BaseResponseDTO<Property>> createProperty(
            @RequestBody PropertyRequestDTO request) {

        try {
            // Count existing properties for this owner
            long propertyCount = propertyService.countPropertiesByOwner(request.getOwnerID());

            // Generate property ID
            String propertyId = propertyService.generatePropertyId(
                    request.getType(), request.getOwnerID(), propertyCount);

            // Create property
            Property property = new Property();
            property.setPropertyID(propertyId);
            property.setPropertyName(request.getPropertyName());
            property.setType(request.getType());
            property.setProvince(request.getProvince());
            property.setAddress(request.getAddress());
            property.setDescription(request.getDescription());
            property.setOwnerID(request.getOwnerID());
            property.setOwnerName(request.getOwnerName());
            property.setTotalRoom(0);
            property.setActiveStatus(1);
            property.setIncome(0);

            Property savedProperty = propertyService.createProperty(property);

            // Check for duplicate room types
            if (request.getRoomTypes() != null && !request.getRoomTypes().isEmpty()) {
                List<RoomType> roomTypesToCreate = new ArrayList<>();

                for (RoomTypeRequestDTO rtDto : request.getRoomTypes()) {
                    // Check duplicate
                    if (roomTypeService.isDuplicateRoomType(savedProperty, rtDto.getName(), rtDto.getFloor())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new BaseResponseDTO<>(
                                        HttpStatus.BAD_REQUEST.value(),
                                        "Duplicate room type found: " + rtDto.getName() + " on floor " + rtDto.getFloor(),
                                        null
                                ));
                    }

                    // Create room type
                    RoomType roomType = new RoomType();
                    String roomTypeId = roomTypeService.generateRoomTypeId(propertyCount, rtDto.getName(), rtDto.getFloor());
                    roomType.setRoomTypeID(roomTypeId);
                    roomType.setName(rtDto.getName());
                    roomType.setPrice(rtDto.getPrice());
                    roomType.setDescription(rtDto.getDescription());
                    roomType.setCapacity(rtDto.getCapacity());
                    roomType.setFacility(rtDto.getFacility());
                    roomType.setFloor(rtDto.getFloor());
                    roomType.setProperty(savedProperty);

                    RoomType savedRoomType = roomTypeService.createRoomType(roomType);

                    // Create rooms for this room type
                    for (int i = 1; i <= rtDto.getUnit(); i++) {
                        Room room = new Room();
                        String roomId = roomService.generateRoomId(propertyId, (rtDto.getFloor() * 100) + i);
                        room.setRoomID(roomId);
                        room.setName(String.valueOf((rtDto.getFloor() * 100) + i));
                        room.setRoomType(savedRoomType);
                        roomService.createRoom(room);
                    }

                    roomTypesToCreate.add(savedRoomType);
                }

                // Update total rooms
                savedProperty.setTotalRoom(propertyService.calculateTotalRooms(savedProperty));
                savedProperty = propertyService.updateProperty(savedProperty);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.CREATED.value(),
                            "Property created successfully",
                            savedProperty
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error creating property: " + e.getMessage(),
                            null
                    ));
        }
    }

    // PUT update property
    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO<Property>> updateProperty(
            @RequestBody Property propertyRequest) {

        Optional<Property> existingProperty = propertyService.getPropertyById(propertyRequest.getPropertyID());

        if (existingProperty.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.NOT_FOUND.value(),
                            "Property not found",
                            null
                    ));
        }

        Property property = existingProperty.get();
        property.setPropertyName(propertyRequest.getPropertyName());
        property.setAddress(propertyRequest.getAddress());
        property.setDescription(propertyRequest.getDescription());

        // Update room types if provided
        if (propertyRequest.getListRoomType() != null) {
            for (RoomType roomType : propertyRequest.getListRoomType()) {
                Optional<RoomType> existingRoomType = roomTypeService.getRoomTypeById(roomType.getRoomTypeID());
                if (existingRoomType.isPresent()) {
                    RoomType rt = existingRoomType.get();
                    rt.setCapacity(roomType.getCapacity());
                    rt.setPrice(roomType.getPrice());
                    rt.setDescription(roomType.getDescription());
                    rt.setFacility(roomType.getFacility());
                    roomTypeService.updateRoomType(rt);
                }
            }
        }

        Property updatedProperty = propertyService.updateProperty(property);

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Property updated successfully",
                updatedProperty
        ));
    }

    // DELETE soft delete property
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponseDTO<Void>> deleteProperty(@PathVariable String id) {
        if (!propertyService.canDeleteProperty(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Cannot delete property with future bookings",
                            null
                    ));
        }

        Property deletedProperty = propertyService.deleteProperty(id);

        if (deletedProperty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.NOT_FOUND.value(),
                            "Property not found",
                            null
                    ));
        }

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Property deleted successfully",
                null
        ));
    }

    // GET count all properties
    @GetMapping("/count")
    public ResponseEntity<BaseResponseDTO<Long>> countProperties() {
        long count = propertyService.countProperties();
        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                count
        ));
    }
}
