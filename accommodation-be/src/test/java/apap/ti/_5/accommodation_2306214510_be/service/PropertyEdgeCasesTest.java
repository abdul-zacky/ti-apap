package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyEdgeCasesTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private AccommodationBookingRepository bookingRepository;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private Property property;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        property = new Property();
        property.setPropertyID("HOT-1234-001");
        property.setPropertyName("Test Hotel");
        property.setType(1);
        property.setOwnerID(ownerId);
        property.setActiveStatus(1);
        property.setProvince("DKI Jakarta");
        property.setTotalRoom(10);
    }

    @Test
    void testGeneratePropertyId_Hotel_SmallNumber() {
        String result = propertyService.generatePropertyId(1, ownerId, 1);
        
        assertTrue(result.startsWith("HOT-"));
        assertTrue(result.endsWith("-002")); // propertyCount + 1
        assertTrue(result.matches("HOT-[a-f0-9]{4}-002"));
    }

    @Test
    void testGeneratePropertyId_Hotel_LargeNumber() {
        String result = propertyService.generatePropertyId(1, ownerId, 999);
        
        assertTrue(result.startsWith("HOT-"));
        assertTrue(result.endsWith("-1000")); // propertyCount + 1 = 1000
        assertTrue(result.matches("HOT-[a-f0-9]{4}-1000"));
    }

    @Test
    void testGeneratePropertyId_Villa_SmallNumber() {
        String result = propertyService.generatePropertyId(2, ownerId, 1);
        
        assertTrue(result.startsWith("VIL-"));
        assertTrue(result.endsWith("-002")); // propertyCount + 1
        assertTrue(result.matches("VIL-[a-f0-9]{4}-002"));
    }

    @Test
    void testGeneratePropertyId_Villa_MediumNumber() {
        String result = propertyService.generatePropertyId(2, ownerId, 50);
        
        assertTrue(result.startsWith("VIL-"));
        assertTrue(result.endsWith("-051")); // propertyCount + 1
        assertTrue(result.matches("VIL-[a-f0-9]{4}-051"));
    }

    @Test
    void testGeneratePropertyId_Apartment_SmallNumber() {
        String result = propertyService.generatePropertyId(3, ownerId, 1);
        
        assertTrue(result.startsWith("APT-"));
        assertTrue(result.endsWith("-002")); // propertyCount + 1
        assertTrue(result.matches("APT-[a-f0-9]{4}-002"));
    }

    @Test
    void testGeneratePropertyId_Apartment_LargeNumber() {
        String result = propertyService.generatePropertyId(3, ownerId, 500);
        
        assertTrue(result.startsWith("APT-"));
        assertTrue(result.endsWith("-501")); // propertyCount + 1
        assertTrue(result.matches("APT-[a-f0-9]{4}-501"));
    }

    @Test
    void testCreateProperty() {
        when(propertyRepository.save(any())).thenReturn(property);
        
        Property result = propertyService.createProperty(property);
        
        assertNotNull(result);
        assertEquals(property.getPropertyID(), result.getPropertyID());
        verify(propertyRepository).save(property);
    }

    @Test
    void testGetPropertyById_Found() {
        when(propertyRepository.findById(anyString())).thenReturn(Optional.of(property));
        
        Optional<Property> result = propertyService.getPropertyById(property.getPropertyID());
        
        assertTrue(result.isPresent());
        assertEquals(property.getPropertyID(), result.get().getPropertyID());
    }

    @Test
    void testGetPropertyById_NotFound() {
        when(propertyRepository.findById(anyString())).thenReturn(Optional.empty());
        
        Optional<Property> result = propertyService.getPropertyById("INVALID-ID");
        
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateProperty() {
        when(propertyRepository.save(any())).thenReturn(property);
        
        Property result = propertyService.updateProperty(property);
        
        assertNotNull(result);
        verify(propertyRepository).save(property);
    }

    @Test
    void testCountProperties() {
        when(propertyRepository.count()).thenReturn(25L);
        
        long result = propertyService.countProperties();
        
        assertEquals(25L, result);
    }

    @Test
    void testCountPropertiesByOwner() {
        when(propertyRepository.countByOwnerID(ownerId)).thenReturn(5L);
        
        long result = propertyService.countPropertiesByOwner(ownerId);
        
        assertEquals(5L, result);
    }

    @Test
    void testCalculateTotalRooms_WithRooms() {
        // Create room types with rooms
        RoomType roomType1 = new RoomType();
        Room room1 = new Room();
        Room room2 = new Room();
        roomType1.setListRoom(Arrays.asList(room1, room2));
        
        RoomType roomType2 = new RoomType();
        Room room3 = new Room();
        roomType2.setListRoom(Arrays.asList(room3));
        
        property.setListRoomType(Arrays.asList(roomType1, roomType2));
        
        int result = propertyService.calculateTotalRooms(property);
        
        assertEquals(3, result); // 2 + 1 = 3
    }

    @Test
    void testCalculateTotalRooms_NoRooms() {
        property.setTotalRoom(0);
        
        int result = propertyService.calculateTotalRooms(property);
        
        assertEquals(0, result);
    }

    @Test
    void testCanDeleteProperty_NoFutureBookings() {
        when(bookingRepository.findByPropertyIDAndCheckInDateAfter(anyString(), any()))
                .thenReturn(Collections.emptyList());
        
        boolean result = propertyService.canDeleteProperty(property.getPropertyID());
        
        assertTrue(result);
    }

    @Test
    void testCanDeleteProperty_HasFutureBookings() {
        AccommodationBooking futureBooking = new AccommodationBooking();
        when(bookingRepository.findByPropertyIDAndCheckInDateAfter(anyString(), any()))
                .thenReturn(Arrays.asList(futureBooking));
        
        boolean result = propertyService.canDeleteProperty(property.getPropertyID());
        
        assertFalse(result);
    }

    @Test
    void testSearchProperties_AllThreeFilters() {
        when(propertyRepository.findByPropertyNameContainingIgnoreCaseAndTypeAndActiveStatusOrderByUpdatedDateDesc(
                "Test", 1, 1)).thenReturn(Arrays.asList(property));
        
        List<Property> result = propertyService.searchProperties("Test", 1, 1);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSearchProperties_EmptyResult() {
        when(propertyRepository.findByPropertyNameContainingIgnoreCaseOrderByUpdatedDateDesc("NonExistent"))
                .thenReturn(Collections.emptyList());
        
        List<Property> result = propertyService.searchProperties("NonExistent", null, null);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchProperties_MultipleResults() {
        Property property2 = new Property();
        property2.setPropertyID("HOT-1234-002");
        property2.setPropertyName("Test Hotel 2");
        
        when(propertyRepository.findByPropertyNameContainingIgnoreCaseOrderByUpdatedDateDesc("Test"))
                .thenReturn(Arrays.asList(property, property2));
        
        List<Property> result = propertyService.searchProperties("Test", null, null);
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllProperties_Empty() {
        when(propertyRepository.findAllByOrderByUpdatedDateDesc()).thenReturn(Collections.emptyList());
        
        List<Property> result = propertyService.getAllProperties();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllProperties_Multiple() {
        Property property2 = new Property();
        property2.setPropertyID("HOT-1234-002");
        
        when(propertyRepository.findAllByOrderByUpdatedDateDesc())
                .thenReturn(Arrays.asList(property, property2));
        
        List<Property> result = propertyService.getAllProperties();
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testSearchProperties_ByType_Hotel() {
        when(propertyRepository.findByTypeOrderByUpdatedDateDesc(1))
                .thenReturn(Arrays.asList(property));
        
        List<Property> result = propertyService.searchProperties(null, 1, null);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSearchProperties_ByType_Villa() {
        property.setType(2);
        when(propertyRepository.findByTypeOrderByUpdatedDateDesc(2))
                .thenReturn(Arrays.asList(property));
        
        List<Property> result = propertyService.searchProperties(null, 2, null);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSearchProperties_ByType_Apartment() {
        property.setType(3);
        when(propertyRepository.findByTypeOrderByUpdatedDateDesc(3))
                .thenReturn(Arrays.asList(property));
        
        List<Property> result = propertyService.searchProperties(null, 3, null);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSearchProperties_ByStatus_Active() {
        when(propertyRepository.findByActiveStatusOrderByUpdatedDateDesc(1))
                .thenReturn(Arrays.asList(property));
        
        List<Property> result = propertyService.searchProperties(null, null, 1);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSearchProperties_ByStatus_Inactive() {
        property.setActiveStatus(0);
        when(propertyRepository.findByActiveStatusOrderByUpdatedDateDesc(0))
                .thenReturn(Arrays.asList(property));
        
        List<Property> result = propertyService.searchProperties(null, null, 0);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDeleteProperty_ChangesStatus() {
        when(propertyRepository.findById(anyString())).thenReturn(Optional.of(property));
        when(propertyRepository.save(any())).thenReturn(property);
        
        Property result = propertyService.deleteProperty(property.getPropertyID());
        
        assertNotNull(result);
        verify(propertyRepository).save(any());
    }

    @Test
    void testDeleteProperty_NotFound() {
        when(propertyRepository.findById(anyString())).thenReturn(Optional.empty());
        
        Property result = propertyService.deleteProperty("INVALID-ID");
        
        assertNull(result);
        verify(propertyRepository, never()).save(any());
    }
}
