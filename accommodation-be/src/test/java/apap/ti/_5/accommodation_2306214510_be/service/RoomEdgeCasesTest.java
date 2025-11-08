package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.RoomRepository;
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
class RoomEdgeCasesTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AccommodationBookingRepository bookingRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;
    private RoomType roomType;
    private Property property;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyID("HOT-1234-001");

        roomType = new RoomType();
        roomType.setRoomTypeID("RT-001");
        roomType.setProperty(property);

        room = new Room();
        room.setRoomID("HOT-1234-001-101");
        room.setName("101");
        room.setRoomType(roomType);
    }

    @Test
    void testGenerateRoomId_FirstFloor() {
        String result = roomService.generateRoomId("HOT-1234-001", 101);
        
        assertEquals("HOT-1234-001-101", result);
    }

    @Test
    void testGenerateRoomId_SecondFloor() {
        String result = roomService.generateRoomId("HOT-1234-001", 201);
        
        assertEquals("HOT-1234-001-201", result);
    }

    @Test
    void testGenerateRoomId_TenthFloor() {
        String result = roomService.generateRoomId("HOT-1234-001", 1001);
        
        assertEquals("HOT-1234-001-1001", result);
    }

    @Test
    void testGenerateRoomId_Villa() {
        String result = roomService.generateRoomId("VIL-5678-002", 101);
        
        assertEquals("VIL-5678-002-101", result);
    }

    @Test
    void testGenerateRoomId_Apartment() {
        String result = roomService.generateRoomId("APT-9012-003", 505);
        
        assertEquals("APT-9012-003-505", result);
    }

    @Test
    void testCreateRoom() {
        when(roomRepository.save(any())).thenReturn(room);
        
        Room result = roomService.createRoom(room);
        
        assertNotNull(result);
        assertEquals(room.getRoomID(), result.getRoomID());
        verify(roomRepository).save(room);
    }

    @Test
    void testGetRoomById_Found() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        
        Optional<Room> result = roomService.getRoomById(room.getRoomID());
        
        assertTrue(result.isPresent());
        assertEquals(room.getRoomID(), result.get().getRoomID());
    }

    @Test
    void testGetRoomById_NotFound() {
        when(roomRepository.findById(anyString())).thenReturn(Optional.empty());
        
        Optional<Room> result = roomService.getRoomById("INVALID-ID");
        
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateRoom() {
        when(roomRepository.save(any())).thenReturn(room);
        
        Room result = roomService.updateRoom(room);
        
        assertNotNull(result);
        verify(roomRepository).save(room);
    }

    @Test
    void testGetAllRooms_Empty() {
        when(roomRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<Room> result = roomService.getAllRooms();
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllRooms_Multiple() {
        Room room2 = new Room();
        room2.setRoomID("HOT-1234-001-102");
        
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room, room2));
        
        List<Room> result = roomService.getAllRooms();
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetRoomsByRoomType() {
        when(roomRepository.findByRoomType(roomType)).thenReturn(Arrays.asList(room));
        
        List<Room> result = roomService.getRoomsByRoomType(roomType);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository).findByRoomType(roomType);
    }

    @Test
    void testGetRoomsByPropertyId() {
        when(roomRepository.findByPropertyID(anyString())).thenReturn(Arrays.asList(room));
        
        List<Room> result = roomService.getRoomsByPropertyId("HOT-1234-001");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository).findByPropertyID("HOT-1234-001");
    }

    @Test
    void testGetRoomsByPropertyId_Empty() {
        when(roomRepository.findByPropertyID(anyString())).thenReturn(Collections.emptyList());
        
        List<Room> result = roomService.getRoomsByPropertyId("HOT-1234-001");
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testIsRoomAvailable_Available() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);
        
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        when(bookingRepository.findConflictingBookings(anyString(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.findConflictingBookingsNative(anyString(), any(), any()))
                .thenReturn(Collections.emptyList());
        
        boolean result = roomService.isRoomAvailable(room.getRoomID(), checkIn, checkOut);
        
        assertTrue(result);
    }

    @Test
    void testIsRoomAvailable_NotAvailable_RoomNotFound() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);
        
        when(roomRepository.findById(anyString())).thenReturn(Optional.empty());
        
        boolean result = roomService.isRoomAvailable("INVALID-ID", checkIn, checkOut);
        
        assertFalse(result);
    }

    @Test
    void testIsRoomAvailable_NotAvailable_HasConflict() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);
        
        AccommodationBooking conflictingBooking = new AccommodationBooking();
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        when(bookingRepository.findConflictingBookings(anyString(), any(), any()))
                .thenReturn(Arrays.asList(conflictingBooking));
        
        boolean result = roomService.isRoomAvailable(room.getRoomID(), checkIn, checkOut);
        
        assertFalse(result);
    }

    @Test
    void testUpdateRoomMaintenance() {
        LocalDateTime maintenanceStart = LocalDateTime.of(2025, 11, 20, 8, 0);
        LocalDateTime maintenanceEnd = LocalDateTime.of(2025, 11, 22, 18, 0);
        
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(room));
        when(roomRepository.save(any())).thenReturn(room);
        
        Room result = roomService.updateRoomMaintenance(room.getRoomID(), maintenanceStart, maintenanceEnd);
        
        assertNotNull(result);
        verify(roomRepository).save(any());
    }

    @Test
    void testUpdateRoomMaintenance_NotFound() {
        LocalDateTime maintenanceStart = LocalDateTime.of(2025, 11, 20, 8, 0);
        LocalDateTime maintenanceEnd = LocalDateTime.of(2025, 11, 22, 18, 0);
        
        when(roomRepository.findById(anyString())).thenReturn(Optional.empty());
        
        Room result = roomService.updateRoomMaintenance("INVALID-ID", maintenanceStart, maintenanceEnd);
        
        assertNull(result);
        verify(roomRepository, never()).save(any());
    }

    @Test
    void testCreateMultipleRooms_TwoRooms() {
        Room room2 = new Room();
        room2.setRoomID("HOT-1234-001-102");
        
        when(roomRepository.save(any())).thenReturn(room).thenReturn(room2);
        
        List<Room> result = roomService.createMultipleRooms(Arrays.asList(room, room2));
        
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roomRepository, times(2)).save(any());
    }

    @Test
    void testCreateMultipleRooms_Empty() {
        List<Room> result = roomService.createMultipleRooms(Collections.emptyList());
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roomRepository, never()).save(any());
    }

    @Test
    void testCountRoomsByPropertyAndFloor_Floor1() {
        Room room1 = new Room();
        room1.setName("101");
        Room room2 = new Room();
        room2.setName("102");
        Room room3 = new Room();
        room3.setName("201");
        
        when(roomRepository.findByPropertyID(anyString()))
                .thenReturn(Arrays.asList(room1, room2, room3));
        
        long result = roomService.countRoomsByPropertyAndFloor("HOT-1234-001", 1);
        
        assertEquals(2, result);
    }

    @Test
    void testCountRoomsByPropertyAndFloor_Floor2() {
        Room room1 = new Room();
        room1.setName("101");
        Room room2 = new Room();
        room2.setName("201");
        Room room3 = new Room();
        room3.setName("202");
        
        when(roomRepository.findByPropertyID(anyString()))
                .thenReturn(Arrays.asList(room1, room2, room3));
        
        long result = roomService.countRoomsByPropertyAndFloor("HOT-1234-001", 2);
        
        assertEquals(2, result);
    }

    @Test
    void testCountRoomsByPropertyAndFloor_NoRoomsOnFloor() {
        Room room1 = new Room();
        room1.setName("101");
        Room room2 = new Room();
        room2.setName("102");
        
        when(roomRepository.findByPropertyID(anyString()))
                .thenReturn(Arrays.asList(room1, room2));
        
        long result = roomService.countRoomsByPropertyAndFloor("HOT-1234-001", 5);
        
        assertEquals(0, result);
    }

    @Test
    void testCountRoomsByPropertyAndFloor_InvalidRoomName() {
        Room room1 = new Room();
        room1.setName("InvalidName");
        
        when(roomRepository.findByPropertyID(anyString()))
                .thenReturn(Arrays.asList(room1));
        
        long result = roomService.countRoomsByPropertyAndFloor("HOT-1234-001", 1);
        
        assertEquals(0, result);
    }
}
