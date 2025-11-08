package apap.ti._5.accommodation_2306214510_be.schedule;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.service.AccommodationBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingStatusSchedulerTest {

    @Mock
    private AccommodationBookingService bookingService;

    @InjectMocks
    private BookingStatusScheduler bookingStatusScheduler;

    @Test
    void testUpdateBookingStatus_ConfirmedToDone() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-001");
        booking.setStatus(1); // Confirmed
        booking.setCheckInDate(LocalDateTime.now().minusDays(1));
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService).updateBookingStatus("BOOK-001", 4);
    }

    @Test
    void testUpdateBookingStatus_WaitingToCancelled() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-002");
        booking.setStatus(0); // Waiting
        booking.setCheckInDate(LocalDateTime.now().minusHours(1));
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService).updateBookingStatus("BOOK-002", 2);
    }

    @Test
    void testUpdateBookingStatus_RefundToCancelled() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-003");
        booking.setStatus(3); // Refund
        booking.setCheckInDate(LocalDateTime.now().minusDays(2));
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService).updateBookingStatus("BOOK-003", 2);
    }

    @Test
    void testUpdateBookingStatus_FutureCheckIn() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-004");
        booking.setStatus(1); // Confirmed
        booking.setCheckInDate(LocalDateTime.now().plusDays(1));
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService, never()).updateBookingStatus(anyString(), anyInt());
    }

    @Test
    void testUpdateBookingStatus_AlreadyDone() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-005");
        booking.setStatus(4); // Done
        booking.setCheckInDate(LocalDateTime.now().minusDays(1));
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService, never()).updateBookingStatus(anyString(), anyInt());
    }

    @Test
    void testUpdateBookingStatus_AlreadyCancelled() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-006");
        booking.setStatus(2); // Cancelled
        booking.setCheckInDate(LocalDateTime.now().minusDays(1));
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService, never()).updateBookingStatus(anyString(), anyInt());
    }

    @Test
    void testUpdateBookingStatus_MultipleBookings() {
        AccommodationBooking booking1 = new AccommodationBooking();
        booking1.setBookingID("BOOK-001");
        booking1.setStatus(1);
        booking1.setCheckInDate(LocalDateTime.now().minusDays(1));
        
        AccommodationBooking booking2 = new AccommodationBooking();
        booking2.setBookingID("BOOK-002");
        booking2.setStatus(0);
        booking2.setCheckInDate(LocalDateTime.now().minusHours(2));
        
        AccommodationBooking booking3 = new AccommodationBooking();
        booking3.setBookingID("BOOK-003");
        booking3.setStatus(1);
        booking3.setCheckInDate(LocalDateTime.now().plusDays(1));
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking1, booking2, booking3));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService).updateBookingStatus("BOOK-001", 4);
        verify(bookingService).updateBookingStatus("BOOK-002", 2);
        verify(bookingService, times(2)).updateBookingStatus(anyString(), anyInt());
    }

    @Test
    void testUpdateBookingStatus_EmptyList() {
        when(bookingService.getAllBookings()).thenReturn(new ArrayList<>());
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService, never()).updateBookingStatus(anyString(), anyInt());
    }

    @Test
    void testUpdateBookingStatus_ExactCheckInTime() {
        AccommodationBooking booking = new AccommodationBooking();
        booking.setBookingID("BOOK-007");
        booking.setStatus(1);
        booking.setCheckInDate(LocalDateTime.now());
        
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(booking));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService).updateBookingStatus("BOOK-007", 4);
    }

    @Test
    void testUpdateBookingStatus_Exception() {
        when(bookingService.getAllBookings()).thenThrow(new RuntimeException("Database error"));
        
        bookingStatusScheduler.updateBookingStatusAutomatically();
        
        verify(bookingService, never()).updateBookingStatus(anyString(), anyInt());
    }
}
