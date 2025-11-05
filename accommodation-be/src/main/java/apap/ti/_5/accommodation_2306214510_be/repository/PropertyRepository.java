package apap.ti._5.accommodation_2306214510_be.repository;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, String> {

    // Find all properties ordered by updated date descending
    List<Property> findAllByOrderByUpdatedDateDesc();

    // Find properties by name containing (case-insensitive)
    List<Property> findByPropertyNameContainingIgnoreCaseOrderByUpdatedDateDesc(String propertyName);

    // Find properties by type
    List<Property> findByTypeOrderByUpdatedDateDesc(int type);

    // Find properties by active status
    List<Property> findByActiveStatusOrderByUpdatedDateDesc(int activeStatus);

    // Find properties by name and type
    List<Property> findByPropertyNameContainingIgnoreCaseAndTypeOrderByUpdatedDateDesc(String propertyName, int type);

    // Find properties by name and status
    List<Property> findByPropertyNameContainingIgnoreCaseAndActiveStatusOrderByUpdatedDateDesc(String propertyName, int activeStatus);

    // Find properties by type and status
    List<Property> findByTypeAndActiveStatusOrderByUpdatedDateDesc(int type, int activeStatus);

    // Find properties by name, type, and status
    List<Property> findByPropertyNameContainingIgnoreCaseAndTypeAndActiveStatusOrderByUpdatedDateDesc(String propertyName, int type, int activeStatus);

    // Find by owner ID
    List<Property> findByOwnerID(UUID ownerID);

    // Count properties by owner ID
    long countByOwnerID(UUID ownerID);

    // Find active properties only
    List<Property> findByActiveStatus(int activeStatus);
}
