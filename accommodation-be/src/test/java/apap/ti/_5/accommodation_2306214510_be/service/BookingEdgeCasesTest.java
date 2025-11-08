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
class BookingEdgeCasesTest {

    @Mock
    private AccommodationBookingRepository bookingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private AccommodationBookingServiceImpl bookingService;

    private AccommodationBooking booking;
    private Room room;
    private RoomType roomType;
    private Property property;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyID("HOT-1234-001");
        property.setIncome(0);

        roomType = new RoomType();
        roomType.setRoomTypeID("RT-001");
        roomType.setPrice(500000);
        roomType.setProperty(property);

        room = new Room();
        room.setRoomID("HOT-1234-001-101");
        room.setName("101");
        room.setRoomType(roomType);

        booking = new AccommodationBooking();
        booking.setBookingID("BOOK-001-101-2025-11-10-14:00:00");
        booking.setRoom(room);
        booking.setStatus(0);
        booking.setTotalPrice(1500000);
        booking.setCheckInDate(LocalDateTime.of(2025, 11, 10, 14, 0));
        booking.setCheckOutDate(LocalDateTime.of(2025, 11, 15, 12, 0));
        booking.setCustomerID(UUID.randomUUID());
    }

    @Test
    void testGenerateBookingId_StandardFormat() {
        LocalDateTime bookingTime = LocalDateTime.of(2025, 11, 8, 15, 30, 45);
        
        String result = bookingService.generateBookingId("HOT-1234-001-101", bookingTime);
        
        assertTrue(result.startsWith("BOOK-001-101"));
        assertTrue(result.contains("2025-11-08-15:30:45"));
    }

    @Test
    void testGenerateBookingId_DifferentPropertyTypes() {
        LocalDateTime bookingTime = LocalDateTime.of(2025, 11, 8, 10, 0, 0);
        
        String hotelId = bookingService.generateBookingId("HOT-5678-002-201", bookingTime);
        String villaId = bookingService.generateBookingId("VIL-9012-003-301", bookingTime);
        String aptId = bookingService.generateBookingId("APT-3456-004-401", bookingTime);
        
        assertTrue(hotelId.startsWith("BOOK-002-201"));
        assertTrue(villaId.startsWith("BOOK-003-301"));
        assertTrue(aptId.startsWith("BOOK-004-401"));
    }

    @Test
    void testCalculateTotalDays_OneDayStay() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 11, 12, 0);
        
        int result = bookingService.calculateTotalDays(checkIn, checkOut);
        
        assertEquals(1, result);
    }

    @Test
    void testCalculateTotalDays_WeekStay() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 17, 12, 0);
        
        int result = bookingService.calculateTotalDays(checkIn, checkOut);
        
        assertEquals(7, result);
    }

    @Test
    void testCalculateTotalDays_MonthStay() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 1, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 12, 1, 12, 0);
        
        int result = bookingService.calculateTotalDays(checkIn, checkOut);
        
        assertEquals(30, result);
    }

    @Test
    void testCalculateTotalPrice_OneNightWithBreakfast() {
        int result = bookingService.calculateTotalPrice(room, 1, true);
        
        assertEquals(550000, result);
    }

    @Test
    void testCalculateTotalPrice_OneNightWithoutBreakfast() {
        int result = bookingService.calculateTotalPrice(room, 1, false);
        
        assertEquals(500000, result);
    }

    @Test
    void testCalculateTotalPrice_SevenNightsWithBreakfast() {
        int result = bookingService.calculateTotalPrice(room, 7, true);
        
        assertEquals(3850000, result);
    }

    @Test
    void testCalculateTotalPrice_SevenNightsWithoutBreakfast() {
        int result = bookingService.calculateTotalPrice(room, 7, false);
        
        assertEquals(3500000, result);
    }

    @Test
    void testPayBooking_Success() {
        booking.setStatus(0);
        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));
        when(propertyRepository.findById(anyString())).thenReturn(Optional.of(property));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(propertyRepository.save(any())).thenReturn(property);
        
        AccommodationBooking result = bookingService.payBooking(booking.getBookingID());
        
        assertNotNull(result);
        verify(bookingRepository).save(any());
        verify(propertyRepository).save(any());
    }

    @Test
    void testPayBooking_BookingNotFound() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.empty());
        
        AccommodationBooking result = bookingService.payBooking("INVALID-ID");
        
        assertNull(result);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCancelBooking_FromWaitingStatus() {
        booking.setStatus(0);
        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        
        AccommodationBooking result = bookingService.cancelBooking(booking.getBookingID());
        
        assertNotNull(result);
        verify(bookingRepository).save(any());
    }

    @Test
    void testCancelBooking_FromConfirmedStatus() {
        booking.setStatus(1);
        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));
        when(propertyRepository.findById(anyString())).thenReturn(Optional.of(property));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(propertyRepository.save(any())).thenReturn(property);
        
        AccommodationBooking result = bookingService.cancelBooking(booking.getBookingID());
        
        assertNotNull(result);
        verify(propertyRepository).save(any());
    }

    @Test
    void testCancelBooking_NotFound() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.empty());
        
        AccommodationBooking result = bookingService.cancelBooking("INVALID-ID");
        
        assertNull(result);
    }

    @Test
    void testRefundBooking_Success() {
        booking.setStatus(2);
        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        
        AccommodationBooking result = bookingService.refundBooking(booking.getBookingID());
        
        assertNotNull(result);
        verify(bookingRepository).save(any());
    }

    @Test
    void testRefundBooking_NotFound() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.empty());
        
        AccommodationBooking result = bookingService.refundBooking("INVALID-ID");
        
        assertNull(result);
    }

    @Test
    void testUpdateBookingStatus_Success() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        
        AccommodationBooking result = bookingService.updateBookingStatus(booking.getBookingID(), 1);
        
        assertNotNull(result);
        verify(bookingRepository).save(any());
    }

    @Test
    void testUpdateBookingStatus_NotFound() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.empty());
        
        AccommodationBooking result = bookingService.updateBookingStatus("INVALID-ID", 1);
        
        assertNull(result);
    }

    @Test
    void testCanCancelBooking_WaitingStatus() {
        booking.setStatus(0);
        
        boolean result = bookingService.canCancelBooking(booking);
        
        assertTrue(result);
    }

    @Test
    void testCanCancelBooking_ConfirmedStatus() {
        booking.setStatus(1);
        
        boolean result = bookingService.canCancelBooking(booking);
        
        assertTrue(result);
    }

    @Test
    void testCanCancelBooking_CancelledStatus() {
        booking.setStatus(2);
        
        boolean result = bookingService.canCancelBooking(booking);
        
        assertFalse(result);
    }

    @Test
    void testCanCancelBooking_RefundedStatus() {
        booking.setStatus(3); // Status 3 = Request refund
        
        boolean result = bookingService.canCancelBooking(booking);
        
        assertTrue(result); // Status 3 can be cancelled
    }

    @Test
    void testCanCancelBooking_DoneStatus() {
        booking.setStatus(4);
        
        boolean result = bookingService.canCancelBooking(booking);
        
        assertFalse(result);
    }

    @Test
    void testGetAllBookings() {
        when(bookingRepository.findAllByOrderByBookingID()).thenReturn(Arrays.asList(booking));
        
        List<AccommodationBooking> result = bookingService.getAllBookings();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository).findAllByOrderByBookingID();
    }

    @Test
    void testGetBookingById_Found() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(booking));
        
        Optional<AccommodationBooking> result = bookingService.getBookingById(booking.getBookingID());
        
        assertTrue(result.isPresent());
        assertEquals(booking.getBookingID(), result.get().getBookingID());
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.empty());
        
        Optional<AccommodationBooking> result = bookingService.getBookingById("INVALID-ID");
        
        assertFalse(result.isPresent());
    }

    @Test
    void testGetBookingsByStatus() {
        when(bookingRepository.findByStatusOrderByBookingID(1)).thenReturn(Arrays.asList(booking));
        
        List<AccommodationBooking> result = bookingService.getBookingsByStatus(1);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetBookingsByCustomer() {
        UUID customerId = UUID.randomUUID();
        when(bookingRepository.findByCustomerIDOrderByBookingID(customerId)).thenReturn(Arrays.asList(booking));
        
        List<AccommodationBooking> result = bookingService.getBookingsByCustomer(customerId);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCountAllBookings() {
        when(bookingRepository.count()).thenReturn(42L);
        
        long result = bookingService.countAllBookings();
        
        assertEquals(42L, result);
    }
}
