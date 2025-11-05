package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final AccommodationBookingRepository bookingRepository;

    // Constructor Dependency Injection
    public PropertyServiceImpl(PropertyRepository propertyRepository,
                               AccommodationBookingRepository bookingRepository) {
        this.propertyRepository = propertyRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Property createProperty(Property property) {
        property.setCreatedDate(LocalDateTime.now());
        property.setUpdatedDate(LocalDateTime.now());
        property.setActiveStatus(1); // Active by default
        property.setIncome(0); // Initial income is 0
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAllByOrderByUpdatedDateDesc();
    }

    @Override
    public Optional<Property> getPropertyById(String id) {
        return propertyRepository.findById(id);
    }

    @Override
    public List<Property> searchProperties(String name, Integer type, Integer status) {
        if (name != null && type != null && status != null) {
            return propertyRepository.findByPropertyNameContainingIgnoreCaseAndTypeAndActiveStatusOrderByUpdatedDateDesc(
                    name, type, status);
        } else if (name != null && type != null) {
            return propertyRepository.findByPropertyNameContainingIgnoreCaseAndTypeOrderByUpdatedDateDesc(name, type);
        } else if (name != null && status != null) {
            return propertyRepository.findByPropertyNameContainingIgnoreCaseAndActiveStatusOrderByUpdatedDateDesc(
                    name, status);
        } else if (type != null && status != null) {
            return propertyRepository.findByTypeAndActiveStatusOrderByUpdatedDateDesc(type, status);
        } else if (name != null) {
            return propertyRepository.findByPropertyNameContainingIgnoreCaseOrderByUpdatedDateDesc(name);
        } else if (type != null) {
            return propertyRepository.findByTypeOrderByUpdatedDateDesc(type);
        } else if (status != null) {
            return propertyRepository.findByActiveStatusOrderByUpdatedDateDesc(status);
        } else {
            return propertyRepository.findAllByOrderByUpdatedDateDesc();
        }
    }

    @Override
    public long countProperties() {
        return propertyRepository.count();
    }

    @Override
    public long countPropertiesByOwner(UUID ownerId) {
        return propertyRepository.countByOwnerID(ownerId);
    }

    @Override
    public Property updateProperty(Property property) {
        property.setUpdatedDate(LocalDateTime.now());
        return propertyRepository.save(property);
    }

    @Override
    public Property deleteProperty(String id) {
        Optional<Property> propertyOpt = propertyRepository.findById(id);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            property.setActiveStatus(0); // Soft delete
            property.setUpdatedDate(LocalDateTime.now());

            // Set all rooms to inactive
            property.getListRoomType().forEach(roomType -> {
                roomType.getListRoom().forEach(room -> {
                    room.setActiveRoom(0);
                    room.setUpdatedDate(LocalDateTime.now());
                });
            });

            return propertyRepository.save(property);
        }
        return null;
    }

    @Override
    public String generatePropertyId(int type, UUID ownerId, long propertyCount) {
        String prefix;
        switch (type) {
            case 1:
                prefix = "HOT";
                break;
            case 2:
                prefix = "VIL";
                break;
            case 3:
                prefix = "APT";
                break;
            default:
                prefix = "UNK";
        }

        // Get last 4 characters of UUID
        String ownerIdStr = ownerId.toString().replace("-", "");
        String last4 = ownerIdStr.substring(ownerIdStr.length() - 4);

        // 3 digit counter
        String counter = String.format("%03d", propertyCount + 1);

        return prefix + "-" + last4 + "-" + counter;
    }

    @Override
    public int calculateTotalRooms(Property property) {
        if (property.getListRoomType() == null || property.getListRoomType().isEmpty()) {
            return 0;
        }

        return property.getListRoomType().stream()
                .mapToInt(roomType -> roomType.getListRoom() != null ? roomType.getListRoom().size() : 0)
                .sum();
    }

    @Override
    public boolean canDeleteProperty(String propertyId) {
        // Check if there are any future bookings
        LocalDateTime now = LocalDateTime.now();
        var futureBookings = bookingRepository.findByPropertyIDAndCheckInDateAfter(propertyId, now);
        return futureBookings.isEmpty();
    }
}
