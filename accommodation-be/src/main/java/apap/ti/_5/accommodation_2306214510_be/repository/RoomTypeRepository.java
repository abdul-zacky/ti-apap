package apap.ti._5.accommodation_2306214510_be.repository;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, String> {

    // Find room types by property
    List<RoomType> findByProperty(Property property);

    // Find room types by property ID
    List<RoomType> findByPropertyPropertyID(String propertyID);

    // Find room type by property, name, and floor (for duplicate check)
    Optional<RoomType> findByPropertyAndNameAndFloor(Property property, String name, int floor);

    // Check if room type exists with property, name, and floor
    boolean existsByPropertyAndNameAndFloor(Property property, String name, int floor);

    // Find all room types ordered by created date
    List<RoomType> findAllByOrderByCreatedDateDesc();
}
