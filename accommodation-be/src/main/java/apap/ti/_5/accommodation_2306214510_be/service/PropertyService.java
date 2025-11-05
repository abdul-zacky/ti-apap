package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.Property;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PropertyService {

    // Create
    Property createProperty(Property property);

    // Read
    List<Property> getAllProperties();
    Optional<Property> getPropertyById(String id);
    List<Property> searchProperties(String name, Integer type, Integer status);
    long countProperties();
    long countPropertiesByOwner(UUID ownerId);

    // Update
    Property updateProperty(Property property);

    // Soft Delete
    Property deleteProperty(String id);

    // Business Logic
    String generatePropertyId(int type, UUID ownerId, long propertyCount);
    int calculateTotalRooms(Property property);
    boolean canDeleteProperty(String propertyId);
}
