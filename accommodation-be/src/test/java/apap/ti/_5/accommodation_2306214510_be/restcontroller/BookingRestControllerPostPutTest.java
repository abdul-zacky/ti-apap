package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.request.BookingRequestDTO;
import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingRestControllerPostPutTest {

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

    private BookingRequestDTO bookingRequestDTO;
    private AccommodationBooking booking;
    private Room room;
    private RoomType roomType;
    private Property property;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyID("HOT-001");
        property.setIncome(1000000);

        roomType = new RoomType();
        roomType.setRoomTypeID("RT-HOT-001-1-001");
        roomType.setProperty(property);
        roomType.setPrice(250000);

        room = new Room();
        room.setRoomID("HOT-abc-001-1-001");
        room.setRoomType(roomType);

        bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setRoomID("HOT-abc-001-1-001");
        bookingRequestDTO.setCustomerID(UUID.randomUUID());
        bookingRequestDTO.setCustomerName("John Doe");
        bookingRequestDTO.setCustomerEmail("john@example.com");
        bookingRequestDTO.setCustomerPhone("08123456789");
        bookingRequestDTO.setCheckInDate(LocalDateTime.now().plusDays(1));
        bookingRequestDTO.setCheckOutDate(LocalDateTime.now().plusDays(3));
        bookingRequestDTO.setBreakfast(false);
        bookingRequestDTO.setCapacity(2);

        booking = new AccommodationBooking();
        booking.setBookingID("BKG-HOT-abc-001-20240101-001");
        booking.setRoom(room);
        booking.setCustomerName("John Doe");
        booking.setCustomerEmail("john@example.com");
        booking.setCheckInDate(LocalDateTime.now().plusDays(1));
        booking.setCheckOutDate(LocalDateTime.now().plusDays(3));
        booking.setTotalPrice(500000);
        booking.setStatus(0);
    }

    // POST /create tests
    @Test
    void testCreateBooking_Success() {
        when(roomService.getRoomById(any())).thenReturn(Optional.of(room));
        when(roomService.isRoomAvailable(any(), any(), any())).thenReturn(true);
        when(bookingService.generateBookingId(any(), any())).thenReturn("BKG-HOT-abc-001-20240101-001");
        when(bookingService.calculateTotalDays(any(), any())).thenReturn(2);
        when(bookingService.calculateTotalPrice(any(), anyInt(), anyBoolean())).thenReturn(500000);
        when(bookingService.createBooking(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.createBooking(bookingRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
        verify(bookingService).createBooking(any());
    }

    @Test
    void testCreateBooking_RoomNotFound() {
        when(roomService.getRoomById(any())).thenReturn(Optional.empty());
        
        ResponseEntity response = bookingRestController.createBooking(bookingRequestDTO);
        
        assertEquals(404, response.getStatusCodeValue());
        verify(bookingService, never()).createBooking(any());
    }

    @Test
    void testCreateBooking_InvalidDates() {
        bookingRequestDTO.setCheckOutDate(LocalDateTime.now().minusDays(1));
        bookingRequestDTO.setCheckInDate(LocalDateTime.now());
        when(roomService.getRoomById(any())).thenReturn(Optional.of(room));
        
        ResponseEntity response = bookingRestController.createBooking(bookingRequestDTO);
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testCreateBooking_RoomNotAvailable() {
        when(roomService.getRoomById(any())).thenReturn(Optional.of(room));
        when(roomService.isRoomAvailable(any(), any(), any())).thenReturn(false);
        
        ResponseEntity response = bookingRestController.createBooking(bookingRequestDTO);
        
        assertEquals(400, response.getStatusCodeValue());
        verify(bookingService, never()).createBooking(any());
    }

    @Test
    void testCreateBooking_WithBreakfast() {
        bookingRequestDTO.setBreakfast(true);
        when(roomService.getRoomById(any())).thenReturn(Optional.of(room));
        when(roomService.isRoomAvailable(any(), any(), any())).thenReturn(true);
        when(bookingService.generateBookingId(any(), any())).thenReturn("BKG1");
        when(bookingService.calculateTotalDays(any(), any())).thenReturn(2);
        when(bookingService.calculateTotalPrice(any(), anyInt(), anyBoolean())).thenReturn(600000);
        when(bookingService.createBooking(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.createBooking(bookingRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateBooking_Exception() {
        when(roomService.getRoomById(any())).thenThrow(new RuntimeException("Database error"));
        
        ResponseEntity response = bookingRestController.createBooking(bookingRequestDTO);
        
        assertEquals(500, response.getStatusCodeValue());
    }

    // PUT /confirm tests
    @Test
    void testConfirmPayment_Success() {
        booking.setStatus(0);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(bookingService.payBooking(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.confirmPayment("BKG1");
        
        assertEquals(200, response.getStatusCodeValue());
        verify(bookingService).payBooking(any());
    }

    @Test
    void testConfirmPayment_NotFound() {
        when(bookingService.getBookingById(any())).thenReturn(Optional.empty());
        
        ResponseEntity response = bookingRestController.confirmPayment("BKG1");
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testConfirmPayment_InvalidStatus() {
        booking.setStatus(1);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.confirmPayment("BKG1");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testConfirmPayment_Exception() {
        booking.setStatus(0);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(bookingService.payBooking(any())).thenThrow(new RuntimeException("Error"));
        
        ResponseEntity response = bookingRestController.confirmPayment("BKG1");
        
        assertEquals(500, response.getStatusCodeValue());
    }

    // PUT /cancel tests
    @Test
    void testCancelBooking_Success() {
        booking.setStatus(0);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(bookingService.cancelBooking(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.cancelBooking("BKG1");
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCancelBooking_NotFound() {
        when(bookingService.getBookingById(any())).thenReturn(Optional.empty());
        
        ResponseEntity response = bookingRestController.cancelBooking("BKG1");
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCancelBooking_AlreadyCancelled() {
        booking.setStatus(2);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.cancelBooking("BKG1");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testCancelBooking_AlreadyRefunded() {
        booking.setStatus(3);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.cancelBooking("BKG1");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testCancelBooking_AlreadyDone() {
        booking.setStatus(4);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.cancelBooking("BKG1");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testCancelBooking_Exception() {
        booking.setStatus(0);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(bookingService.cancelBooking(any())).thenThrow(new RuntimeException("Error"));
        
        ResponseEntity response = bookingRestController.cancelBooking("BKG1");
        
        assertEquals(500, response.getStatusCodeValue());
    }

    // PUT /complete tests
    @Test
    void testCompleteBooking_Success() {
        booking.setStatus(1);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(bookingService.updateBookingDetails(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.completeBooking("BKG1");
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCompleteBooking_NotFound() {
        when(bookingService.getBookingById(any())).thenReturn(Optional.empty());
        
        ResponseEntity response = bookingRestController.completeBooking("BKG1");
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testCompleteBooking_InvalidStatus() {
        booking.setStatus(0);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.completeBooking("BKG1");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testCompleteBooking_Exception() {
        booking.setStatus(1);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(bookingService.updateBookingDetails(any())).thenThrow(new RuntimeException("Error"));
        
        ResponseEntity response = bookingRestController.completeBooking("BKG1");
        
        assertEquals(500, response.getStatusCodeValue());
    }

    // PUT /refund tests
    @Test
    void testProcessRefund_Success() {
        booking.setStatus(3);
        booking.setExtraPay(-100000);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        doNothing().when(bookingService).updatePropertyIncome(any(), anyInt(), anyBoolean());
        when(bookingRepository.save(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.processRefund("BKG1");
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testProcessRefund_NotFound() {
        when(bookingService.getBookingById(any())).thenReturn(Optional.empty());
        
        ResponseEntity response = bookingRestController.processRefund("BKG1");
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testProcessRefund_InvalidStatus() {
        booking.setStatus(1);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.processRefund("BKG1");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testProcessRefund_NoRefundAmount() {
        booking.setStatus(3);
        booking.setExtraPay(0);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.processRefund("BKG1");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testProcessRefund_PositiveExtraPay() {
        booking.setStatus(3);
        booking.setExtraPay(50000);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.processRefund("BKG1");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testProcessRefund_Exception() {
        booking.setStatus(3);
        booking.setExtraPay(-100000);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any())).thenThrow(new RuntimeException("Error"));
        
        ResponseEntity response = bookingRestController.processRefund("BKG1");
        
        assertEquals(500, response.getStatusCodeValue());
    }

    // PUT /update tests
    @Test
    void testUpdateBooking_Success_SamePrice() {
        booking.setStatus(1);
        booking.setTotalPrice(500000);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(roomRepository.findById(any())).thenReturn(Optional.of(room));
        when(bookingRepository.save(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.updateBooking("BKG1", bookingRequestDTO);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateBooking_NotFound() {
        when(bookingService.getBookingById(any())).thenReturn(Optional.empty());
        
        ResponseEntity response = bookingRestController.updateBooking("BKG1", bookingRequestDTO);
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateBooking_InvalidStatus() {
        booking.setStatus(0);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        
        ResponseEntity response = bookingRestController.updateBooking("BKG1", bookingRequestDTO);
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testUpdateBooking_RoomNotFound() {
        booking.setStatus(1);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(roomRepository.findById(any())).thenReturn(Optional.empty());
        
        ResponseEntity response = bookingRestController.updateBooking("BKG1", bookingRequestDTO);
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateBooking_ExtraPayment() {
        booking.setStatus(1);
        booking.setTotalPrice(300000);
        roomType.setPrice(500000);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(roomRepository.findById(any())).thenReturn(Optional.of(room));
        when(bookingRepository.save(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.updateBooking("BKG1", bookingRequestDTO);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateBooking_Refund() {
        booking.setStatus(1);
        booking.setTotalPrice(1000000);
        roomType.setPrice(100000);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(roomRepository.findById(any())).thenReturn(Optional.of(room));
        when(bookingRepository.save(any())).thenReturn(booking);
        
        ResponseEntity response = bookingRestController.updateBooking("BKG1", bookingRequestDTO);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateBooking_Exception() {
        booking.setStatus(1);
        when(bookingService.getBookingById(any())).thenReturn(Optional.of(booking));
        when(roomRepository.findById(any())).thenThrow(new RuntimeException("Error"));
        
        ResponseEntity response = bookingRestController.updateBooking("BKG1", bookingRequestDTO);
        
        assertEquals(500, response.getStatusCodeValue());
    }

    // GET /count tests
    @Test
    void testCountBookings() {
        when(bookingService.countAllBookings()).thenReturn(100L);
        
        ResponseEntity response = bookingRestController.countBookings();
        
        assertEquals(200, response.getStatusCodeValue());
    }

    // GET /chart tests
    @Test
    void testGetBookingChart_Success() {
        List<Property> properties = Arrays.asList(property);
        when(propertyService.getAllProperties()).thenReturn(properties);
        
        ResponseEntity response = bookingRestController.getBookingChart(null, null);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingChart_WithMonthYear() {
        List<Property> properties = Arrays.asList(property);
        when(propertyService.getAllProperties()).thenReturn(properties);
        
        ResponseEntity response = bookingRestController.getBookingChart(1, 2024);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingChart_Exception() {
        when(propertyService.getAllProperties()).thenThrow(new RuntimeException("Error"));
        
        ResponseEntity response = bookingRestController.getBookingChart(null, null);
        
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingChart_EmptyProperties() {
        when(propertyService.getAllProperties()).thenReturn(new ArrayList<>());
        
        ResponseEntity response = bookingRestController.getBookingChart(null, null);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingChart_NoIncome() {
        property.setIncome(0);
        List<Property> properties = Arrays.asList(property);
        when(propertyService.getAllProperties()).thenReturn(properties);
        
        ResponseEntity response = bookingRestController.getBookingChart(null, null);
        
        assertEquals(200, response.getStatusCodeValue());
    }
}
