package apap.ti._5.accommodation_2306214510_be.service;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AccommodationBookingRepository bookingRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room testRoom;
    private RoomType testRoomType;

    @BeforeEach
    void setUp() {
        testRoomType = new RoomType();
        testRoomType.setRoomTypeID("RT-001");
        testRoomType.setName("Deluxe");

        testRoom = new Room();
        testRoom.setRoomID("HOT-1234-001-101");
        testRoom.setName("101");
        testRoom.setRoomType(testRoomType);
    }

    @Test
    void testCreateRoom() {
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);

        Room result = roomService.createRoom(testRoom);

        assertNotNull(result);
        assertEquals("HOT-1234-001-101", result.getRoomID());
        assertEquals("101", result.getName());
        verify(roomRepository, times(1)).save(testRoom);
    }

    @Test
    void testGetRoomById() {
        when(roomRepository.findById("HOT-1234-001-101")).thenReturn(Optional.of(testRoom));

        Optional<Room> result = roomService.getRoomById("HOT-1234-001-101");

        assertTrue(result.isPresent());
        assertEquals("101", result.get().getName());
        verify(roomRepository, times(1)).findById("HOT-1234-001-101");
    }

    @Test
    void testGetRoomById_NotFound() {
        when(roomRepository.findById("INVALID")).thenReturn(Optional.empty());

        Optional<Room> result = roomService.getRoomById("INVALID");

        assertFalse(result.isPresent());
        verify(roomRepository, times(1)).findById("INVALID");
    }

    @Test
    void testUpdateRoom() {
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);

        Room result = roomService.updateRoom(testRoom);

        assertNotNull(result);
        assertEquals("HOT-1234-001-101", result.getRoomID());
        verify(roomRepository, times(1)).save(testRoom);
    }

    @Test
    void testGenerateRoomId() {
        String result = roomService.generateRoomId("HOT-1234-001", 101);

        assertEquals("HOT-1234-001-101", result);
    }

    @Test
    void testIsRoomAvailable_Available() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);

        when(roomRepository.findById("HOT-1234-001-101")).thenReturn(Optional.of(testRoom));
        when(bookingRepository.findConflictingBookings(
                eq("HOT-1234-001-101"), eq(checkIn), eq(checkOut)))
                .thenReturn(java.util.Collections.emptyList());

        boolean result = roomService.isRoomAvailable("HOT-1234-001-101", checkIn, checkOut);

        assertTrue(result);
        verify(roomRepository, times(1)).findById("HOT-1234-001-101");
        verify(bookingRepository, times(1)).findConflictingBookings(eq("HOT-1234-001-101"), eq(checkIn), eq(checkOut));
    }

    @Test
    void testIsRoomAvailable_NotAvailable() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);

        when(roomRepository.findById("HOT-1234-001-101")).thenReturn(Optional.empty());

        boolean result = roomService.isRoomAvailable("HOT-1234-001-101", checkIn, checkOut);

        assertFalse(result);
        verify(roomRepository, times(1)).findById("HOT-1234-001-101");
    }

    @Test
    void testScheduleMaintenance() {
        LocalDateTime maintenanceStart = LocalDateTime.of(2025, 11, 20, 8, 0);
        LocalDateTime maintenanceEnd = LocalDateTime.of(2025, 11, 22, 18, 0);

        when(roomRepository.findById("HOT-1234-001-101")).thenReturn(Optional.of(testRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);

        Room result = roomService.updateRoomMaintenance("HOT-1234-001-101", maintenanceStart, maintenanceEnd);

        assertNotNull(result);
        verify(roomRepository, times(1)).findById("HOT-1234-001-101");
        verify(roomRepository, times(1)).save(any(Room.class));
    }

    @Test
    void testGetAllRooms() {
        when(roomRepository.findAll()).thenReturn(java.util.Arrays.asList(testRoom));

        java.util.List<Room> result = roomService.getAllRooms();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void testGetRoomsByRoomType() {
        when(roomRepository.findByRoomType(testRoomType)).thenReturn(java.util.Arrays.asList(testRoom));

        java.util.List<Room> result = roomService.getRoomsByRoomType(testRoomType);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository, times(1)).findByRoomType(testRoomType);
    }

    @Test
    void testGetRoomsByPropertyId() {
        when(roomRepository.findByPropertyID("HOT-1234-001")).thenReturn(java.util.Arrays.asList(testRoom));

        java.util.List<Room> result = roomService.getRoomsByPropertyId("HOT-1234-001");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository, times(1)).findByPropertyID("HOT-1234-001");
    }

    @Test
    void testCreateMultipleRooms() {
        Room room2 = new Room();
        room2.setRoomID("HOT-1234-001-102");
        room2.setName("102");
        
        java.util.List<Room> rooms = java.util.Arrays.asList(testRoom, room2);
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom).thenReturn(room2);

        java.util.List<Room> result = roomService.createMultipleRooms(rooms);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roomRepository, times(2)).save(any(Room.class));
    }

    @Test
    void testCountRoomsByPropertyAndFloor() {
        Room floorRoom1 = new Room();
        floorRoom1.setName("201");
        Room floorRoom2 = new Room();
        floorRoom2.setName("202");
        Room otherFloorRoom = new Room();
        otherFloorRoom.setName("301");

        when(roomRepository.findByPropertyID("HOT-1234-001"))
                .thenReturn(java.util.Arrays.asList(floorRoom1, floorRoom2, otherFloorRoom));

        long result = roomService.countRoomsByPropertyAndFloor("HOT-1234-001", 2);

        assertEquals(2, result);
        verify(roomRepository, times(1)).findByPropertyID("HOT-1234-001");
    }

    @Test
    void testGenerateRoomId_WithDifferentFloors() {
        assertEquals("HOT-1234-001-201", roomService.generateRoomId("HOT-1234-001", 201));
        assertEquals("HOT-1234-001-301", roomService.generateRoomId("HOT-1234-001", 301));
        assertEquals("HOT-1234-001-1001", roomService.generateRoomId("HOT-1234-001", 1001));
    }

    @Test
    void testGetAvailableRooms() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);

        when(roomRepository.findAll()).thenReturn(java.util.Arrays.asList(testRoom));
        when(roomRepository.findById(anyString())).thenReturn(Optional.of(testRoom));
        when(bookingRepository.findConflictingBookings(anyString(), any(), any()))
                .thenReturn(java.util.Collections.emptyList());
        when(bookingRepository.findConflictingBookingsNative(anyString(), any(), any()))
                .thenReturn(java.util.Collections.emptyList());

        java.util.List<Room> result = roomService.getAvailableRooms(checkIn, checkOut);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository, times(1)).findAll();
    }
}
