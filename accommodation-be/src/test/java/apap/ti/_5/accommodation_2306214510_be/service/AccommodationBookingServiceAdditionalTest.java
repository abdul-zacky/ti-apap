package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AccommodationBookingServiceAdditionalTest {

    @Mock
    private AccommodationBookingRepository bookingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private AccommodationBookingServiceImpl bookingService;

    private AccommodationBooking booking;
    private Property property;
    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        property = new Property();
        property.setPropertyID("PROP-001");
        property.setIncome(0);

        room = new Room();
        room.setRoomID("ROOM-001");

        booking = new AccommodationBooking();
        booking.setBookingID("BOOK-001");
        booking.setCheckInDate(LocalDateTime.now().plusDays(2));
        booking.setCheckOutDate(LocalDateTime.now().plusDays(5));
        booking.setTotalPrice(1000000);
        booking.setStatus(0); // Pending
        booking.setRoom(room);
    }

    // Test processCheckInUpdates
    @Test
    void testProcessCheckInUpdates_NoBookings() {
        when(bookingRepository.findBookingsToMarkAsDone(any())).thenReturn(Collections.emptyList());
        when(bookingRepository.findBookingsToCancel(any())).thenReturn(Collections.emptyList());
        
        bookingService.processCheckInUpdates();
        
        verify(bookingRepository, times(1)).findBookingsToMarkAsDone(any());
        verify(bookingRepository, times(1)).findBookingsToCancel(any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testProcessCheckInUpdates_WithBookingsToMarkDone() {
        booking.setStatus(3); // Has status 3
        booking.setRefund(0);
        apap.ti._5.accommodation_2306214510_be.model.RoomType roomType = 
            new apap.ti._5.accommodation_2306214510_be.model.RoomType();
        roomType.setProperty(property);
        room.setRoomType(roomType);
        booking.setRoom(room);
        
        when(bookingRepository.findBookingsToMarkAsDone(any())).thenReturn(Arrays.asList(booking));
        when(bookingRepository.findBookingsToCancel(any())).thenReturn(Collections.emptyList());
        when(bookingRepository.save(any())).thenReturn(booking);
        
        bookingService.processCheckInUpdates();
        
        verify(bookingRepository, times(1)).save(any());
        assertEquals(4, booking.getStatus()); // Should be marked as done
    }

    @Test
    void testProcessCheckInUpdates_WithBookingsToCancel() {
        booking.setStatus(0); // Pending
        booking.setExtraPay(100000);
        booking.setTotalPrice(500000);
        apap.ti._5.accommodation_2306214510_be.model.RoomType roomType = 
            new apap.ti._5.accommodation_2306214510_be.model.RoomType();
        roomType.setProperty(property);
        room.setRoomType(roomType);
        booking.setRoom(room);
        
        when(bookingRepository.findBookingsToMarkAsDone(any())).thenReturn(Collections.emptyList());
        when(bookingRepository.findBookingsToCancel(any())).thenReturn(Arrays.asList(booking));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(propertyRepository.save(any())).thenReturn(property);
        
        bookingService.processCheckInUpdates();
        
        verify(bookingRepository, times(1)).save(any());
        assertEquals(2, booking.getStatus()); // Should be cancelled
    }

    // Test getBookingStatistics
    @Test
    void testGetBookingStatistics_NoProperties() {
        when(propertyRepository.findAllByOrderByUpdatedDateDesc()).thenReturn(Collections.emptyList());
        
        Map<String, Integer> stats = bookingService.getBookingStatistics(12, 2024);
        
        assertNotNull(stats);
        assertTrue(stats.isEmpty());
    }

    @Test
    void testGetBookingStatistics_WithData() {
        property.setPropertyName("Grand Hotel");
        booking.setTotalPrice(1000000);
        booking.setStatus(4); // Done
        
        when(propertyRepository.findAllByOrderByUpdatedDateDesc()).thenReturn(Arrays.asList(property));
        when(bookingRepository.findDoneBookingsByPropertyAndMonth("PROP-001", 12, 2024))
            .thenReturn(Arrays.asList(booking));
        
        Map<String, Integer> stats = bookingService.getBookingStatistics(12, 2024);
        
        assertNotNull(stats);
        assertEquals(1000000, stats.get("Grand Hotel"));
    }

    @Test
    void testGetBookingStatistics_MultipleProperties() {
        Property property2 = new Property();
        property2.setPropertyID("PROP-002");
        property2.setPropertyName("Beach Resort");
        
        property.setPropertyName("City Hotel");
        
        AccommodationBooking booking2 = new AccommodationBooking();
        booking2.setTotalPrice(2000000);
        
        when(propertyRepository.findAllByOrderByUpdatedDateDesc())
            .thenReturn(Arrays.asList(property, property2));
        when(bookingRepository.findDoneBookingsByPropertyAndMonth("PROP-001", 11, 2024))
            .thenReturn(Arrays.asList(booking));
        when(bookingRepository.findDoneBookingsByPropertyAndMonth("PROP-002", 11, 2024))
            .thenReturn(Arrays.asList(booking2));
        
        booking.setTotalPrice(1500000);
        
        Map<String, Integer> stats = bookingService.getBookingStatistics(11, 2024);
        
        assertNotNull(stats);
        assertEquals(2, stats.size());
    }

    // Test getDoneBookingsByMonth
    @Test
    void testGetDoneBookingsByMonth_NoProperties() {
        when(propertyRepository.findAll()).thenReturn(Collections.emptyList());
        
        List<AccommodationBooking> result = bookingService.getDoneBookingsByMonth(12, 2024);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDoneBookingsByMonth_WithBookings() {
        when(propertyRepository.findAll()).thenReturn(Arrays.asList(property));
        when(bookingRepository.findDoneBookingsByPropertyAndMonth("PROP-001", 12, 2024))
            .thenReturn(Arrays.asList(booking));
        
        List<AccommodationBooking> result = bookingService.getDoneBookingsByMonth(12, 2024);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(booking.getBookingID(), result.get(0).getBookingID());
    }

    @Test
    void testGetDoneBookingsByMonth_MultipleProperties() {
        Property property2 = new Property();
        property2.setPropertyID("PROP-002");
        
        AccommodationBooking booking2 = new AccommodationBooking();
        booking2.setBookingID("BOOK-002");
        
        when(propertyRepository.findAll()).thenReturn(Arrays.asList(property, property2));
        when(bookingRepository.findDoneBookingsByPropertyAndMonth("PROP-001", 11, 2024))
            .thenReturn(Arrays.asList(booking));
        when(bookingRepository.findDoneBookingsByPropertyAndMonth("PROP-002", 11, 2024))
            .thenReturn(Arrays.asList(booking2));
        
        List<AccommodationBooking> result = bookingService.getDoneBookingsByMonth(11, 2024);
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // Test handlePriceIncrease
    @Test
    void testHandlePriceIncrease() {
        booking.setExtraPay(0);
        booking.setStatus(1);
        
        bookingService.handlePriceIncrease(booking, 200000);
        
        assertEquals(200000, booking.getExtraPay());
        assertEquals(0, booking.getStatus()); // Back to waiting for payment
    }

    @Test
    void testHandlePriceIncrease_LargeIncrease() {
        booking.setExtraPay(100000);
        booking.setStatus(1);
        
        bookingService.handlePriceIncrease(booking, 500000);
        
        assertEquals(500000, booking.getExtraPay());
        assertEquals(0, booking.getStatus());
    }

    // Test handlePriceDecrease
    @Test
    void testHandlePriceDecrease() {
        booking.setRefund(0);
        booking.setStatus(1);
        
        bookingService.handlePriceDecrease(booking, 300000);
        
        assertEquals(300000, booking.getRefund());
        assertEquals(3, booking.getStatus()); // Request refund
    }

    @Test
    void testHandlePriceDecrease_LargeDecrease() {
        booking.setRefund(50000);
        booking.setStatus(1);
        
        bookingService.handlePriceDecrease(booking, 600000);
        
        assertEquals(600000, booking.getRefund());
        assertEquals(3, booking.getStatus());
    }

    // Additional tests for generateBookingId edge cases
    @Test
    void testGenerateBookingId_StandardFormat() {
        String roomId = "HOT-abc-004-101";
        LocalDateTime checkIn = LocalDateTime.of(2024, 1, 15, 14, 0);
        
        String result = bookingService.generateBookingId(roomId, checkIn);
        
        assertNotNull(result);
        assertTrue(result.startsWith("BOOK-004-101-"));
        assertTrue(result.contains("2024-01-15"));
    }

    @Test
    void testGenerateBookingId_December() {
        String roomId = "VIL-xyz-005-202";
        LocalDateTime checkIn = LocalDateTime.of(2024, 12, 25, 14, 0);
        
        String result = bookingService.generateBookingId(roomId, checkIn);
        
        assertNotNull(result);
        assertTrue(result.startsWith("BOOK-005-202-"));
        assertTrue(result.contains("2024-12-25"));
    }

    @Test
    void testGenerateBookingId_DifferentRoomIds() {
        LocalDateTime checkIn = LocalDateTime.of(2024, 6, 15, 10, 30);
        
        String result1 = bookingService.generateBookingId("HOT-abc-001-101", checkIn);
        String result2 = bookingService.generateBookingId("HOT-abc-002-102", checkIn);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1, result2);
        assertTrue(result1.contains("001-101"));
        assertTrue(result2.contains("002-102"));
    }
}
