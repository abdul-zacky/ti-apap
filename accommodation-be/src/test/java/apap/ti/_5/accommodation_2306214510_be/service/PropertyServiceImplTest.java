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

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private AccommodationBookingRepository bookingRepository;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private Property testProperty;
    private UUID testOwnerId;

    @BeforeEach
    void setUp() {
        testOwnerId = UUID.randomUUID();
        testProperty = new Property();
        testProperty.setPropertyID("HOT-1234-001");
        testProperty.setPropertyName("Test Hotel");
        testProperty.setType(1);
        testProperty.setProvince(31);
        testProperty.setAddress("Test Address");
        testProperty.setDescription("Test Description");
        testProperty.setOwnerID(testOwnerId);
        testProperty.setOwnerName("Test Owner");
        testProperty.setTotalRoom(10);
        testProperty.setActiveStatus(1);
        testProperty.setIncome(0);
    }

    @Test
    void testCreateProperty() {
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);

        Property result = propertyService.createProperty(testProperty);

        assertNotNull(result);
        assertEquals("HOT-1234-001", result.getPropertyID());
        assertEquals("Test Hotel", result.getPropertyName());
        verify(propertyRepository, times(1)).save(testProperty);
    }

    @Test
    void testGetAllProperties() {
        List<Property> properties = Arrays.asList(testProperty);
        when(propertyRepository.findAllByOrderByUpdatedDateDesc()).thenReturn(properties);

        List<Property> result = propertyService.getAllProperties();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Hotel", result.get(0).getPropertyName());
        verify(propertyRepository, times(1)).findAllByOrderByUpdatedDateDesc();
    }

    @Test
    void testGetPropertyById() {
        when(propertyRepository.findById("HOT-1234-001")).thenReturn(Optional.of(testProperty));

        Optional<Property> result = propertyService.getPropertyById("HOT-1234-001");

        assertTrue(result.isPresent());
        assertEquals("Test Hotel", result.get().getPropertyName());
        verify(propertyRepository, times(1)).findById("HOT-1234-001");
    }

    @Test
    void testGetPropertyById_NotFound() {
        when(propertyRepository.findById("INVALID")).thenReturn(Optional.empty());

        Optional<Property> result = propertyService.getPropertyById("INVALID");

        assertFalse(result.isPresent());
        verify(propertyRepository, times(1)).findById("INVALID");
    }

    @Test
    void testSearchProperties_WithAllFilters() {
        List<Property> properties = Arrays.asList(testProperty);
        when(propertyRepository.findByPropertyNameContainingIgnoreCaseAndTypeAndActiveStatusOrderByUpdatedDateDesc(
                "Test", 1, 1)).thenReturn(properties);

        List<Property> result = propertyService.searchProperties("Test", 1, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepository, times(1))
                .findByPropertyNameContainingIgnoreCaseAndTypeAndActiveStatusOrderByUpdatedDateDesc("Test", 1, 1);
    }

    @Test
    void testSearchProperties_WithNameOnly() {
        List<Property> properties = Arrays.asList(testProperty);
        when(propertyRepository.findByPropertyNameContainingIgnoreCaseOrderByUpdatedDateDesc("Test"))
                .thenReturn(properties);

        List<Property> result = propertyService.searchProperties("Test", null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepository, times(1))
                .findByPropertyNameContainingIgnoreCaseOrderByUpdatedDateDesc("Test");
    }

    @Test
    void testSearchProperties_WithTypeAndStatus() {
        List<Property> properties = Arrays.asList(testProperty);
        when(propertyRepository.findByTypeAndActiveStatusOrderByUpdatedDateDesc(1, 1))
                .thenReturn(properties);

        List<Property> result = propertyService.searchProperties(null, 1, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(propertyRepository, times(1))
                .findByTypeAndActiveStatusOrderByUpdatedDateDesc(1, 1);
    }

    @Test
    void testCountProperties() {
        when(propertyRepository.count()).thenReturn(5L);

        long result = propertyService.countProperties();

        assertEquals(5L, result);
        verify(propertyRepository, times(1)).count();
    }

    @Test
    void testCountPropertiesByOwner() {
        when(propertyRepository.countByOwnerID(testOwnerId)).thenReturn(3L);

        long result = propertyService.countPropertiesByOwner(testOwnerId);

        assertEquals(3L, result);
        verify(propertyRepository, times(1)).countByOwnerID(testOwnerId);
    }

    @Test
    void testUpdateProperty() {
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);

        Property result = propertyService.updateProperty(testProperty);

        assertNotNull(result);
        assertEquals("Test Hotel", result.getPropertyName());
        verify(propertyRepository, times(1)).save(testProperty);
    }

    @Test
    void testDeleteProperty() {
        when(propertyRepository.findById("HOT-1234-001")).thenReturn(Optional.of(testProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);

        Property result = propertyService.deleteProperty("HOT-1234-001");

        assertNotNull(result);
        assertEquals(0, result.getActiveStatus());
        verify(propertyRepository, times(1)).findById("HOT-1234-001");
        verify(propertyRepository, times(1)).save(any(Property.class));
    }

    @Test
    void testDeleteProperty_NotFound() {
        when(propertyRepository.findById("INVALID")).thenReturn(Optional.empty());

        Property result = propertyService.deleteProperty("INVALID");

        assertNull(result);
        verify(propertyRepository, times(1)).findById("INVALID");
        verify(propertyRepository, never()).save(any(Property.class));
    }

    @Test
    void testGeneratePropertyId_Hotel() {
        UUID ownerId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        String result = propertyService.generatePropertyId(1, ownerId, 0);

        assertTrue(result.startsWith("HOT-"));
        assertTrue(result.endsWith("-001"));
    }

    @Test
    void testGeneratePropertyId_Villa() {
        UUID ownerId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        String result = propertyService.generatePropertyId(2, ownerId, 0);

        assertTrue(result.startsWith("VIL-"));
    }

    @Test
    void testGeneratePropertyId_Apartment() {
        UUID ownerId = UUID.fromString("12345678-1234-1234-1234-123456789abc");

        String result = propertyService.generatePropertyId(3, ownerId, 2);

        assertTrue(result.startsWith("APT-"));
        assertTrue(result.endsWith("-003"));
    }

    @Test
    void testCalculateTotalRooms() {
        RoomType roomType1 = new RoomType();
        Room room1 = new Room();
        room1.setRoomID("R001");
        Room room2 = new Room();
        room2.setRoomID("R002");
        roomType1.setListRoom(Arrays.asList(room1, room2));

        RoomType roomType2 = new RoomType();
        Room room3 = new Room();
        room3.setRoomID("R003");
        roomType2.setListRoom(Arrays.asList(room3));

        testProperty.setListRoomType(Arrays.asList(roomType1, roomType2));

        int result = propertyService.calculateTotalRooms(testProperty);

        assertEquals(3, result);
    }

    @Test
    void testCalculateTotalRooms_NoRoomTypes() {
        testProperty.setListRoomType(new ArrayList<>());

        int result = propertyService.calculateTotalRooms(testProperty);

        assertEquals(0, result);
    }

    @Test
    void testCanDeleteProperty_NoFutureBookings() {
        when(bookingRepository.findByPropertyIDAndCheckInDateAfter(
                eq("HOT-1234-001"), any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        boolean result = propertyService.canDeleteProperty("HOT-1234-001");

        assertTrue(result);
        verify(bookingRepository, times(1))
                .findByPropertyIDAndCheckInDateAfter(eq("HOT-1234-001"), any(LocalDateTime.class));
    }

    @Test
    void testCanDeleteProperty_HasFutureBookings() {
        AccommodationBooking futureBooking = new AccommodationBooking();
        futureBooking.setCheckInDate(LocalDateTime.now().plusDays(5));

        when(bookingRepository.findByPropertyIDAndCheckInDateAfter(
                eq("HOT-1234-001"), any(LocalDateTime.class)))
                .thenReturn(Arrays.asList(futureBooking));

        boolean result = propertyService.canDeleteProperty("HOT-1234-001");

        assertFalse(result);
        verify(bookingRepository, times(1))
                .findByPropertyIDAndCheckInDateAfter(eq("HOT-1234-001"), any(LocalDateTime.class));
    }
}
