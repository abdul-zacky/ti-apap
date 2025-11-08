package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.repository.RoomRepository;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import apap.ti._5.accommodation_2306214510_be.service.RoomTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RoomRestControllerGetTest {

    @Mock
    private PropertyService propertyService;

    @Mock
    private RoomTypeService roomTypeService;

    @Mock
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomRestController roomRestController;

    private Property property;
    private RoomType roomType;
    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        property = new Property();
        property.setPropertyID("PROP-2024-001");
        property.setPropertyName("Test Property");

        roomType = new RoomType();
        roomType.setRoomTypeID("RT-2024-001");
        roomType.setName("Deluxe Room");
        roomType.setFloor(2);
        roomType.setPrice(500000);
        roomType.setCapacity(2);
        roomType.setProperty(property);

        room = new Room();
        room.setRoomID("ROOM-2024-001");
        room.setName("Room 201");
        room.setActiveRoom(1);
        room.setRoomType(roomType);
    }

    // Test getAllRooms
    @Test
    void testGetAllRooms_Success() {
        List<Room> rooms = Arrays.asList(room);
        when(roomService.getRoomsByPropertyId(anyString())).thenReturn(rooms);
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.of(property));

        ResponseEntity<?> response = roomRestController.getAllRooms("PROP-2024-001");

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllRooms_EmptyList() {
        when(roomService.getRoomsByPropertyId(anyString())).thenReturn(new ArrayList<>());
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.of(property));

        ResponseEntity<?> response = roomRestController.getAllRooms("PROP-2024-001");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllRooms_PropertyNotFound() {
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = roomRestController.getAllRooms("PROP-2024-001");

        assertEquals(404, response.getStatusCodeValue());
    }

    // Test getRoomsByPropertyId
    @Test
    void testGetRoomsByPropertyId_Success() {
        List<RoomType> roomTypes = Arrays.asList(roomType);
        roomType.setListRoom(Arrays.asList(room));
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.of(property));
        when(roomTypeService.getRoomTypesByPropertyId(anyString())).thenReturn(roomTypes);

        ResponseEntity<?> response = roomRestController.getRoomsByPropertyId("PROP-2024-001");

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetRoomsByPropertyId_PropertyNotFound() {
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = roomRestController.getRoomsByPropertyId("PROP-2024-001");

        assertEquals(404, response.getStatusCodeValue());
    }

    // Test checkRoomAvailability
    @Test
    void testCheckRoomAvailability_Success() {
        List<RoomType> roomTypes = Arrays.asList(roomType);
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.of(property));
        when(roomTypeService.getRoomTypesByPropertyId(anyString())).thenReturn(roomTypes);
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        when(roomService.isRoomAvailable(anyString(), any(), any())).thenReturn(true);

        String checkInStr = "2024-12-01T14:00:00";
        String checkOutStr = "2024-12-05T12:00:00";

        ResponseEntity<?> response = roomRestController.checkRoomAvailability(
                "PROP-2024-001", checkInStr, checkOutStr);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCheckRoomAvailability_PropertyNotFound() {
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.empty());

        String checkInStr = "2024-12-01T14:00:00";
        String checkOutStr = "2024-12-05T12:00:00";

        ResponseEntity<?> response = roomRestController.checkRoomAvailability(
                "PROP-2024-001", checkInStr, checkOutStr);

        assertEquals(404, response.getStatusCodeValue());
    }

    // Test checkSingleRoomAvailability
    @Test
    void testCheckSingleRoomAvailability_Available() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        when(roomService.isRoomAvailable(anyString(), any(), any())).thenReturn(true);

        String checkInStr = "2024-12-01T14:00:00";
        String checkOutStr = "2024-12-05T12:00:00";

        ResponseEntity<?> response = roomRestController.checkSingleRoomAvailability(
                "ROOM-2024-001", checkInStr, checkOutStr);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCheckSingleRoomAvailability_NotAvailable() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        when(roomService.isRoomAvailable(anyString(), any(), any())).thenReturn(false);

        String checkInStr = "2024-12-01T14:00:00";
        String checkOutStr = "2024-12-05T12:00:00";

        ResponseEntity<?> response = roomRestController.checkSingleRoomAvailability(
                "ROOM-2024-001", checkInStr, checkOutStr);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCheckSingleRoomAvailability_RoomNotFound() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.empty());

        String checkInStr = "2024-12-01T14:00:00";
        String checkOutStr = "2024-12-05T12:00:00";

        ResponseEntity<?> response = roomRestController.checkSingleRoomAvailability(
                "ROOM-2024-001", checkInStr, checkOutStr);

        assertEquals(404, response.getStatusCodeValue());
    }

    // Test syncTotalRooms
    @Test
    void testSyncTotalRooms_Success() {
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.of(property));
        when(roomTypeService.getRoomTypesByPropertyId(anyString())).thenReturn(new ArrayList<>());
        when(propertyService.calculateTotalRooms(any())).thenReturn(10);
        when(propertyService.updateProperty(any(Property.class))).thenReturn(property);

        ResponseEntity<?> response = roomRestController.syncTotalRooms("PROP-2024-001");

        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService, times(1)).updateProperty(any(Property.class));
    }

    @Test
    void testSyncTotalRooms_PropertyNotFound() {
        when(propertyService.getPropertyById(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = roomRestController.syncTotalRooms("PROP-2024-001");

        assertEquals(404, response.getStatusCodeValue());
    }
}
