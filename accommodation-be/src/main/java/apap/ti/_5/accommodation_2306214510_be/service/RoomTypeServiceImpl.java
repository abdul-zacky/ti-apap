package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    // Constructor Dependency Injection
    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public RoomType createRoomType(RoomType roomType) {
        roomType.setCreatedDate(LocalDateTime.now());
        roomType.setUpdatedDate(LocalDateTime.now());
        return roomTypeRepository.save(roomType);
    }

    @Override
    public List<RoomType> createMultipleRoomTypes(List<RoomType> roomTypes, Property property) {
        List<RoomType> savedRoomTypes = new ArrayList<>();

        for (RoomType roomType : roomTypes) {
            roomType.setProperty(property);
            roomType.setCreatedDate(LocalDateTime.now());
            roomType.setUpdatedDate(LocalDateTime.now());
            savedRoomTypes.add(roomTypeRepository.save(roomType));
        }

        return savedRoomTypes;
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAllByOrderByCreatedDateDesc();
    }

    @Override
    public Optional<RoomType> getRoomTypeById(String id) {
        return roomTypeRepository.findById(id);
    }

    @Override
    public List<RoomType> getRoomTypesByProperty(Property property) {
        return roomTypeRepository.findByProperty(property);
    }

    @Override
    public List<RoomType> getRoomTypesByPropertyId(String propertyId) {
        return roomTypeRepository.findByPropertyPropertyID(propertyId);
    }

    @Override
    public RoomType updateRoomType(RoomType roomType) {
        roomType.setUpdatedDate(LocalDateTime.now());
        return roomTypeRepository.save(roomType);
    }

    @Override
    public String generateRoomTypeId(long propertyCount, String name, int floor) {
        String counter = String.format("%03d", propertyCount + 1);
        return counter + "–" + name + "–" + floor;
    }

    @Override
    public boolean isDuplicateRoomType(Property property, String name, int floor) {
        return roomTypeRepository.existsByPropertyAndNameAndFloor(property, name, floor);
    }

    @Override
    public boolean checkDuplicatesInList(List<RoomType> roomTypes) {
        Set<String> seen = new HashSet<>();

        for (RoomType roomType : roomTypes) {
            String key = roomType.getProperty().getPropertyID() + "-" +
                    roomType.getName() + "-" +
                    roomType.getFloor();

            if (seen.contains(key)) {
                return true; // Duplicate found
            }

            // Check against existing in database
            if (isDuplicateRoomType(roomType.getProperty(), roomType.getName(), roomType.getFloor())) {
                return true;
            }

            seen.add(key);
        }

        return false;
    }
}
