package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.RoomRepository;
import apap.ti._5.accommodation_2306214510_be.service.AccommodationBookingService;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRestControllerComprehensiveTest {

    @Mock
    private AccommodationBookingService bookingService;

    @Mock
    private RoomService roomService;

    @Mock
    private PropertyService propertyService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private AccommodationBookingRepository bookingRepository;

    @InjectMocks
    private BookingRestController bookingRestController;

    private AccommodationBooking booking;
    private Room room;
    private Property property;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyID("HOT-001");
        
        room = new Room();
        room.setRoomID("HOT-001-101");
        
        booking = new AccommodationBooking();
        booking.setBookingID("BOOK-001");
        booking.setStatus(0);
    }

    // Test all status combinations
    @Test
    void testGetAllBookings_Status0() {
        when(bookingService.getBookingsByStatus(0)).thenReturn(Arrays.asList(booking));
        ResponseEntity response = bookingRestController.getAllBookings(null, 0);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_Status1() {
        when(bookingService.getBookingsByStatus(1)).thenReturn(Arrays.asList(booking));
        ResponseEntity response = bookingRestController.getAllBookings(null, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_Status2() {
        when(bookingService.getBookingsByStatus(2)).thenReturn(Arrays.asList(booking));
        ResponseEntity response = bookingRestController.getAllBookings(null, 2);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_Status3() {
        when(bookingService.getBookingsByStatus(3)).thenReturn(Arrays.asList(booking));
        ResponseEntity response = bookingRestController.getAllBookings(null, 3);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_Status4() {
        when(bookingService.getBookingsByStatus(4)).thenReturn(Arrays.asList(booking));
        ResponseEntity response = bookingRestController.getAllBookings(null, 4);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test search combinations
    @Test
    void testGetAllBookings_SearchEmail() {
        when(bookingService.searchBookings("test@example.com", null)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("test@example.com", null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchName() {
        when(bookingService.searchBookings("John", null)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("John", null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchEmailStatus0() {
        when(bookingService.searchBookings("test@example.com", 0)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("test@example.com", 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchEmailStatus1() {
        when(bookingService.searchBookings("test@example.com", 1)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("test@example.com", 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchEmailStatus2() {
        when(bookingService.searchBookings("test@example.com", 2)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("test@example.com", 2);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchEmailStatus3() {
        when(bookingService.searchBookings("test@example.com", 3)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("test@example.com", 3);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchEmailStatus4() {
        when(bookingService.searchBookings("test@example.com", 4)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("test@example.com", 4);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchNameStatus0() {
        when(bookingService.searchBookings("John", 0)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("John", 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchNameStatus1() {
        when(bookingService.searchBookings("John", 1)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("John", 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchNameStatus2() {
        when(bookingService.searchBookings("John", 2)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("John", 2);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchNameStatus3() {
        when(bookingService.searchBookings("John", 3)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("John", 3);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchNameStatus4() {
        when(bookingService.searchBookings("John", 4)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("John", 4);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test multiple bookings
    @Test
    void testGetAllBookings_TenBookings() {
        List<AccommodationBooking> bookings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bookings.add(new AccommodationBooking());
        }
        when(bookingService.getAllBookings()).thenReturn(bookings);
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_FiftyBookings() {
        List<AccommodationBooking> bookings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            bookings.add(new AccommodationBooking());
        }
        when(bookingService.getAllBookings()).thenReturn(bookings);
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_HundredBookings() {
        List<AccommodationBooking> bookings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            bookings.add(new AccommodationBooking());
        }
        when(bookingService.getAllBookings()).thenReturn(bookings);
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test getBookingById variations
    @Test
    void testGetBookingById_BookingId1() {
        booking.setBookingID("BOOK-001");
        when(bookingService.getBookingById("BOOK-001")).thenReturn(Optional.of(booking));
        ResponseEntity response = bookingRestController.getBookingById("BOOK-001");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_BookingId2() {
        booking.setBookingID("BOOK-002");
        when(bookingService.getBookingById("BOOK-002")).thenReturn(Optional.of(booking));
        ResponseEntity response = bookingRestController.getBookingById("BOOK-002");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_BookingId3() {
        booking.setBookingID("BOOK-003");
        when(bookingService.getBookingById("BOOK-003")).thenReturn(Optional.of(booking));
        ResponseEntity response = bookingRestController.getBookingById("BOOK-003");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_NotFound1() {
        when(bookingService.getBookingById("INVALID-1")).thenReturn(Optional.empty());
        ResponseEntity response = bookingRestController.getBookingById("INVALID-1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_NotFound2() {
        when(bookingService.getBookingById("INVALID-2")).thenReturn(Optional.empty());
        ResponseEntity response = bookingRestController.getBookingById("INVALID-2");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_NotFound3() {
        when(bookingService.getBookingById("INVALID-3")).thenReturn(Optional.empty());
        ResponseEntity response = bookingRestController.getBookingById("INVALID-3");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_EmptyString() {
        when(bookingService.getBookingById("")).thenReturn(Optional.empty());
        ResponseEntity response = bookingRestController.getBookingById("");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_LongId() {
        String longId = "BOOK-HOT-101-2025-11-08-15:30:45";
        booking.setBookingID(longId);
        when(bookingService.getBookingById(longId)).thenReturn(Optional.of(booking));
        ResponseEntity response = bookingRestController.getBookingById(longId);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test empty and single item lists
    @Test
    void testGetAllBookings_EmptyList() {
        when(bookingService.getAllBookings()).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SingleBooking() {
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_TwoBookings() {
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking, booking));
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test different search terms
    @Test
    void testGetAllBookings_SearchShortTerm() {
        when(bookingService.searchBookings("ab", null)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("ab", null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchLongTerm() {
        when(bookingService.searchBookings("very long search term with many words", null)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("very long search term with many words", null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchSpecialCharacters() {
        when(bookingService.searchBookings("test@email.com", null)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("test@email.com", null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchWithSpaces() {
        when(bookingService.searchBookings("John Doe", null)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("John Doe", null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchNumbers() {
        when(bookingService.searchBookings("12345", null)).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings("12345", null);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Additional edge cases
    @Test
    void testGetAllBookings_NullSearchNullStatus() {
        when(bookingService.getAllBookings()).thenReturn(new ArrayList<>());
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        assertEquals(200, response.getStatusCodeValue());
        verify(bookingService).getAllBookings();
        verify(bookingService, never()).getBookingsByStatus(anyInt());
        verify(bookingService, never()).searchBookings(anyString(), anyInt());
    }

    @Test
    void testGetBookingById_DifferentFormats() {
        String[] ids = {
            "BOOK-HOT-101",
            "BOOK-VIL-201",
            "BOOK-APT-301",
            "BOOK-001-101",
            "BOOK-002-201"
        };
        
        for (String id : ids) {
            booking.setBookingID(id);
            when(bookingService.getBookingById(id)).thenReturn(Optional.of(booking));
            ResponseEntity response = bookingRestController.getBookingById(id);
            assertEquals(200, response.getStatusCodeValue());
        }
    }

    @Test
    void testGetAllBookings_StatusWithResults() {
        for (int status = 0; status <= 4; status++) {
            List<AccommodationBooking> bookings = Arrays.asList(booking, booking, booking);
            when(bookingService.getBookingsByStatus(status)).thenReturn(bookings);
            ResponseEntity response = bookingRestController.getAllBookings(null, status);
            assertEquals(200, response.getStatusCodeValue());
        }
    }

    @Test
    void testGetAllBookings_StatusWithEmptyResults() {
        for (int status = 0; status <= 4; status++) {
            when(bookingService.getBookingsByStatus(status)).thenReturn(new ArrayList<>());
            ResponseEntity response = bookingRestController.getAllBookings(null, status);
            assertEquals(200, response.getStatusCodeValue());
        }
    }
}
