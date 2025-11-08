package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.RoomRepository;
import apap.ti._5.accommodation_2306214510_be.service.AccommodationBookingService;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRestControllerTest {

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

    @Test
    void testGetAllBookings_NoFilters() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-123-101-2025-11-08-10:00:00");
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(bookingService).getAllBookings();
    }

    @Test
    void testGetAllBookings_WithStatusFilter() {
        when(bookingService.getBookingsByStatus(1)).thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings(null, 1);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(bookingService).getBookingsByStatus(1);
    }

    @Test
    void testGetAllBookings_WithSearchTerm() {
        when(bookingService.searchBookings("customer@email.com", null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings("customer@email.com", null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(bookingService).searchBookings("customer@email.com", null);
    }

    @Test
    void testGetAllBookings_WithBothFilters() {
        when(bookingService.searchBookings("customer@email.com", 1))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings("customer@email.com", 1);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(bookingService).searchBookings("customer@email.com", 1);
    }

    @Test
    void testGetAllBookings_StatusZero() {
        when(bookingService.getBookingsByStatus(0)).thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings(null, 0);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_StatusTwo() {
        when(bookingService.getBookingsByStatus(2)).thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings(null, 2);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_StatusThree() {
        when(bookingService.getBookingsByStatus(3)).thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings(null, 3);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_StatusFour() {
        when(bookingService.getBookingsByStatus(4)).thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings(null, 4);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_Found() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-123-101-2025-11-08-10:00:00");
        
        when(bookingService.getBookingById("BOOK-123-101-2025-11-08-10:00:00"))
            .thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.getBookingById("BOOK-123-101-2025-11-08-10:00:00");
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(bookingService).getBookingById("BOOK-123-101-2025-11-08-10:00:00");
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingService.getBookingById("INVALID-ID"))
            .thenReturn(Optional.empty());
        
        ResponseEntity response = bookingRestController.getBookingById("INVALID-ID");
        
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(bookingService).getBookingById("INVALID-ID");
    }

    @Test
    void testGetAllBookings_MultipleBookings() {
        List<AccommodationBooking> bookings = Arrays.asList(
            new AccommodationBooking(),
            new AccommodationBooking(),
            new AccommodationBooking()
        );
        when(bookingService.getAllBookings()).thenReturn(bookings);
        
        ResponseEntity response = bookingRestController.getAllBookings(null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchByEmail() {
        when(bookingService.searchBookings("test@email.com", null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings("test@email.com", null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchByCustomerName() {
        when(bookingService.searchBookings("John Doe", null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings("John Doe", null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchWithStatusZero() {
        when(bookingService.searchBookings("test", 0))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings("test", 0);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchWithStatusOne() {
        when(bookingService.searchBookings("test", 1))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings("test", 1);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllBookings_SearchWithStatusThree() {
        when(bookingService.searchBookings("test", 3))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getAllBookings("test", 3);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingById_MultipleAttempts() {
        AccommodationBooking booking1 = new AccommodationBooking();
        booking1.setBookingID("BOOK-001");
        
        AccommodationBooking booking2 = new AccommodationBooking();
        booking2.setBookingID("BOOK-002");
        
        when(bookingService.getBookingById("BOOK-001")).thenReturn(Optional.of(booking1));
        when(bookingService.getBookingById("BOOK-002")).thenReturn(Optional.of(booking2));
        
        ResponseEntity response1 = bookingRestController.getBookingById("BOOK-001");
        ResponseEntity response2 = bookingRestController.getBookingById("BOOK-002");
        
        assertNotNull(response1);
        assertNotNull(response2);
        assertEquals(200, response1.getStatusCodeValue());
        assertEquals(200, response2.getStatusCodeValue());
    }
}
