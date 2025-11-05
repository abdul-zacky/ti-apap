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
}
