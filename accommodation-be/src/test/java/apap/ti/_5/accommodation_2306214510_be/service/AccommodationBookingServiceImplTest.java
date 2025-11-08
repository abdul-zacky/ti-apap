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
class AccommodationBookingServiceImplTest {

    @Mock
    private AccommodationBookingRepository bookingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @InjectMocks
    private AccommodationBookingServiceImpl bookingService;

    private AccommodationBooking testBooking;
    private Room testRoom;
    private RoomType testRoomType;
    private Property testProperty;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setPropertyID("HOT-1234-001");
        testProperty.setIncome(0);

        testRoomType = new RoomType();
        testRoomType.setRoomTypeID("RT-001");
        testRoomType.setName("Deluxe");
        testRoomType.setPrice(500000);
        testRoomType.setProperty(testProperty);

        testRoom = new Room();
        testRoom.setRoomID("HOT-1234-001-101");
        testRoom.setName("101");
        testRoom.setRoomType(testRoomType);

        testBooking = new AccommodationBooking();
        testBooking.setBookingID("BOOK-1234001-20251110140000");
        testBooking.setRoom(testRoom);
        testBooking.setCheckInDate(LocalDateTime.of(2025, 11, 10, 14, 0));
        testBooking.setCheckOutDate(LocalDateTime.of(2025, 11, 15, 12, 0));
        testBooking.setCustomerID(UUID.randomUUID());
        testBooking.setCustomerName("John Doe");
        testBooking.setCustomerEmail("john@example.com");
        testBooking.setCustomerPhone("081234567890");
        testBooking.setBreakfast(true);
        testBooking.setCapacity(2);
        testBooking.setTotalPrice(2500000);
        testBooking.setStatus(0);
        testBooking.setExtraPay(0);
    }

    @Test
    void testCreateBooking() {
        when(bookingRepository.save(any(AccommodationBooking.class))).thenReturn(testBooking);

        AccommodationBooking result = bookingService.createBooking(testBooking);

        assertNotNull(result);
        assertEquals("BOOK-1234001-20251110140000", result.getBookingID());
        assertEquals(0, result.getStatus());
        verify(bookingRepository, times(1)).save(testBooking);
    }

    @Test
    void testGetAllBookings() {
        List<AccommodationBooking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.findAllByOrderByBookingID()).thenReturn(bookings);

        List<AccommodationBooking> result = bookingService.getAllBookings();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getCustomerName());
        verify(bookingRepository, times(1)).findAllByOrderByBookingID();
    }

    @Test
    void testGetBookingById() {
        when(bookingRepository.findById("BOOK-1234001-20251110140000"))
                .thenReturn(Optional.of(testBooking));

        Optional<AccommodationBooking> result = bookingService.getBookingById("BOOK-1234001-20251110140000");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getCustomerName());
        verify(bookingRepository, times(1)).findById("BOOK-1234001-20251110140000");
    }

    @Test
    void testGetBookingsByStatus() {
        List<AccommodationBooking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.findByStatusOrderByBookingID(0)).thenReturn(bookings);

        List<AccommodationBooking> result = bookingService.getBookingsByStatus(0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getStatus());
        verify(bookingRepository, times(1)).findByStatusOrderByBookingID(0);
    }

    @Test
    void testGetBookingsByCustomer() {
        UUID customerId = UUID.randomUUID();
        List<AccommodationBooking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.findByCustomerIDOrderByBookingID(customerId)).thenReturn(bookings);

        List<AccommodationBooking> result = bookingService.getBookingsByCustomer(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findByCustomerIDOrderByBookingID(customerId);
    }

    @Test
    void testSearchBookings_WithBothParameters() {
        List<AccommodationBooking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.searchBookingsByStatus("John", 0))
                .thenReturn(bookings);

        List<AccommodationBooking> result = bookingService.searchBookings("John", 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .searchBookingsByStatus("John", 0);
    }

    @Test
    void testSearchBookings_SearchTermOnly() {
        List<AccommodationBooking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.searchBookings("John"))
                .thenReturn(bookings);

        List<AccommodationBooking> result = bookingService.searchBookings("John", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1))
                .searchBookings("John");
    }

    @Test
    void testCountAllBookings() {
        when(bookingRepository.count()).thenReturn(10L);

        long result = bookingService.countAllBookings();

        assertEquals(10L, result);
        verify(bookingRepository, times(1)).count();
    }

    @Test
    void testUpdateBookingDetails() {
        when(bookingRepository.findById(testBooking.getBookingID())).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(AccommodationBooking.class))).thenReturn(testBooking);

        AccommodationBooking result = bookingService.updateBookingDetails(testBooking);

        assertNotNull(result);
        verify(bookingRepository, times(1)).findById(testBooking.getBookingID());
        verify(bookingRepository, times(1)).save(any(AccommodationBooking.class));
    }

    @Test
    void testUpdateBookingStatus() {
        when(bookingRepository.findById("BOOK-1234001-20251110140000"))
                .thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(AccommodationBooking.class))).thenReturn(testBooking);

        AccommodationBooking result = bookingService.updateBookingStatus("BOOK-1234001-20251110140000", 1);

        assertNotNull(result);
        assertEquals(1, result.getStatus());
        verify(bookingRepository, times(1)).findById("BOOK-1234001-20251110140000");
        verify(bookingRepository, times(1)).save(any(AccommodationBooking.class));
    }

    @Test
    void testPayBooking() {
        when(bookingRepository.findById("BOOK-1234001-20251110140000"))
                .thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(AccommodationBooking.class))).thenReturn(testBooking);
        when(propertyRepository.findById("HOT-1234-001")).thenReturn(Optional.of(testProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);

        AccommodationBooking result = bookingService.payBooking("BOOK-1234001-20251110140000");

        assertNotNull(result);
        assertEquals(1, result.getStatus());
        verify(bookingRepository, times(1)).findById("BOOK-1234001-20251110140000");
        verify(propertyRepository, times(1)).findById("HOT-1234-001");
    }

    @Test
    void testCancelBooking_FromWaitingStatus() {
        when(bookingRepository.findById("BOOK-1234001-20251110140000"))
                .thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(AccommodationBooking.class))).thenReturn(testBooking);

        AccommodationBooking result = bookingService.cancelBooking("BOOK-1234001-20251110140000");

        assertNotNull(result);
        assertEquals(2, result.getStatus());
        verify(bookingRepository, times(1)).findById("BOOK-1234001-20251110140000");
    }

    @Test
    void testCancelBooking_FromConfirmedStatus() {
        testBooking.setStatus(1); // Confirmed
        when(bookingRepository.findById("BOOK-1234001-20251110140000"))
                .thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(AccommodationBooking.class))).thenReturn(testBooking);
        when(propertyRepository.findById("HOT-1234-001")).thenReturn(Optional.of(testProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);

        AccommodationBooking result = bookingService.cancelBooking("BOOK-1234001-20251110140000");

        assertNotNull(result);
        assertEquals(2, result.getStatus()); // Cancelled
        verify(bookingRepository, times(1)).findById("BOOK-1234001-20251110140000");
    }

    @Test
    void testGenerateBookingId() {
        LocalDateTime bookingTime = LocalDateTime.of(2025, 11, 10, 14, 0);

        String result = bookingService.generateBookingId("HOT-1234-001-101", bookingTime);

        assertNotNull(result);
        assertTrue(result.startsWith("BOOK-"));
        assertTrue(result.contains("2025"));
    }

    @Test
    void testCalculateTotalDays() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);

        int result = bookingService.calculateTotalDays(checkIn, checkOut);

        assertEquals(5, result);
    }

    @Test
    void testCalculateTotalPrice_WithBreakfast() {
        int result = bookingService.calculateTotalPrice(testRoom, 5, true);

        // Price per night: 500,000
        // Breakfast: 50,000 per day
        // Total: (500,000 + 50,000) * 5 = 2,750,000
        assertEquals(2750000, result);
    }

    @Test
    void testCalculateTotalPrice_WithoutBreakfast() {
        int result = bookingService.calculateTotalPrice(testRoom, 5, false);

        // Price per night: 500,000
        // Total: 500,000 * 5 = 2,500,000
        assertEquals(2500000, result);
    }

    @Test
    void testHasConflictingBooking_HasConflict() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);

        when(bookingRepository.findConflictingBookings("HOT-1234-001-101", checkIn, checkOut))
                .thenReturn(Arrays.asList(testBooking));

        boolean result = bookingService.hasConflictingBooking("HOT-1234-001-101", checkIn, checkOut);

        assertTrue(result);
        verify(bookingRepository, times(1))
                .findConflictingBookings("HOT-1234-001-101", checkIn, checkOut);
    }

    @Test
    void testHasConflictingBooking_NoConflict() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);

        when(bookingRepository.findConflictingBookings("HOT-1234-001-101", checkIn, checkOut))
                .thenReturn(new ArrayList<>());

        boolean result = bookingService.hasConflictingBooking("HOT-1234-001-101", checkIn, checkOut);

        assertFalse(result);
        verify(bookingRepository, times(1))
                .findConflictingBookings("HOT-1234-001-101", checkIn, checkOut);
    }

    @Test
    void testCanUpdateBooking_WaitingStatus() {
        testBooking.setStatus(0);

        boolean result = bookingService.canUpdateBooking(testBooking);

        assertTrue(result);
    }

    @Test
    void testCanUpdateBooking_CancelledStatus() {
        testBooking.setStatus(2);

        boolean result = bookingService.canUpdateBooking(testBooking);

        assertFalse(result);
    }

    @Test
    void testCanCancelBooking_Allowed() {
        testBooking.setStatus(0);

        boolean result = bookingService.canCancelBooking(testBooking);

        assertTrue(result);
    }

    @Test
    void testCanCancelBooking_NotAllowed() {
        testBooking.setStatus(4);

        boolean result = bookingService.canCancelBooking(testBooking);

        assertFalse(result);
    }

    @Test
    void testCalculateTotalDays_SameDay() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 10, 18, 0);

        int result = bookingService.calculateTotalDays(checkIn, checkOut);

        assertEquals(0, result); // Same day = 0 days difference
    }

    @Test
    void testCalculateTotalDays_MultipleDays() {
        LocalDateTime checkIn = LocalDateTime.of(2025, 11, 10, 14, 0);
        LocalDateTime checkOut = LocalDateTime.of(2025, 11, 15, 12, 0);

        int result = bookingService.calculateTotalDays(checkIn, checkOut);

        assertEquals(5, result);
    }

    @Test
    void testCanUpdateBooking_StatusWaiting() {
        testBooking.setStatus(0);

        boolean result = bookingService.canUpdateBooking(testBooking);

        assertTrue(result);
    }

    @Test
    void testCanUpdateBooking_StatusConfirmed() {
        testBooking.setStatus(1);

        boolean result = bookingService.canUpdateBooking(testBooking);

        assertTrue(result);
    }

    @Test
    void testCanUpdateBooking_StatusCancelled() {
        testBooking.setStatus(2);

        boolean result = bookingService.canUpdateBooking(testBooking);

        assertFalse(result);
    }

    @Test
    void testCalculatePriceChange_Increase() {
        AccommodationBooking oldBooking = new AccommodationBooking();
        oldBooking.setTotalPrice(1000000);

        AccommodationBooking newBooking = new AccommodationBooking();
        newBooking.setTotalPrice(1500000);

        int result = bookingService.calculatePriceChange(oldBooking, newBooking);

        assertEquals(500000, result);
    }

    @Test
    void testCalculatePriceChange_Decrease() {
        AccommodationBooking oldBooking = new AccommodationBooking();
        oldBooking.setTotalPrice(1500000);

        AccommodationBooking newBooking = new AccommodationBooking();
        newBooking.setTotalPrice(1000000);

        int result = bookingService.calculatePriceChange(oldBooking, newBooking);

        assertEquals(-500000, result);
    }

    @Test
    void testCalculatePriceChange_NoChange() {
        AccommodationBooking oldBooking = new AccommodationBooking();
        oldBooking.setTotalPrice(1000000);

        AccommodationBooking newBooking = new AccommodationBooking();
        newBooking.setTotalPrice(1000000);

        int result = bookingService.calculatePriceChange(oldBooking, newBooking);

        assertEquals(0, result);
    }
}
