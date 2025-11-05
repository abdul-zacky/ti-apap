package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomTypeService {

    // Create
    RoomType createRoomType(RoomType roomType);
    List<RoomType> createMultipleRoomTypes(List<RoomType> roomTypes, Property property);

    // Read
    List<RoomType> getAllRoomTypes();
    Optional<RoomType> getRoomTypeById(String id);
    List<RoomType> getRoomTypesByProperty(Property property);
    List<RoomType> getRoomTypesByPropertyId(String propertyId);

    // Update
    RoomType updateRoomType(RoomType roomType);

    // Business Logic
    String generateRoomTypeId(long propertyCount, String name, int floor);
    boolean isDuplicateRoomType(Property property, String name, int floor);
    boolean checkDuplicatesInList(List<RoomType> roomTypes);
}
