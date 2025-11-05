package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.BaseResponseDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.BookingRequestDTO;
import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.service.AccommodationBookingService;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*")
public class BookingRestController {

    private final AccommodationBookingService bookingService;
    private final RoomService roomService;
    private final PropertyService propertyService;

    public BookingRestController(AccommodationBookingService bookingService,
                                 RoomService roomService,
                                 PropertyService propertyService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.propertyService = propertyService;
    }

    // GET all bookings with optional filters
    @GetMapping("")
    public ResponseEntity<BaseResponseDTO<List<AccommodationBooking>>> getAllBookings(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Integer status) {

        List<AccommodationBooking> bookings;

        if (searchTerm != null || status != null) {
            bookings = bookingService.searchBookings(searchTerm, status);
        } else {
            bookings = bookingService.getAllBookings();
        }

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                bookings
        ));
    }

    // GET booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDTO<AccommodationBooking>> getBookingById(@PathVariable String id) {
        Optional<AccommodationBooking> booking = bookingService.getBookingById(id);

        if (booking.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.NOT_FOUND.value(),
                            "Booking not found",
                            null
                    ));
        }

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                booking.get()
        ));
    }

    // POST create booking
    @PostMapping("/create")
    public ResponseEntity<BaseResponseDTO<AccommodationBooking>> createBooking(
            @RequestBody BookingRequestDTO request) {

        try {
            // Validate room exists
            Optional<Room> roomOpt = roomService.getRoomById(request.getRoomID());
            if (roomOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Room not found",
                                null
                        ));
            }

            Room room = roomOpt.get();

            // Validate dates
            if (request.getCheckOutDate().isBefore(request.getCheckInDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Check-out date must be after check-in date",
                                null
                        ));
            }

            // Check room availability
            if (!roomService.isRoomAvailable(request.getRoomID(), request.getCheckInDate(), request.getCheckOutDate())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Room is not available for the selected dates",
                                null
                        ));
            }

            // Create booking
            AccommodationBooking booking = new AccommodationBooking();
            String bookingId = bookingService.generateBookingId(request.getRoomID(), request.getCheckInDate());
            booking.setBookingID(bookingId);
            booking.setRoom(room);
            booking.setCheckInDate(request.getCheckInDate());
            booking.setCheckOutDate(request.getCheckOutDate());
            booking.setCustomerID(request.getCustomerID());
            booking.setCustomerName(request.getCustomerName());
            booking.setCustomerEmail(request.getCustomerEmail());
            booking.setCustomerPhone(request.getCustomerPhone());
            booking.setBreakfast(request.isBreakfast());
            booking.setCapacity(request.getCapacity());
            booking.setStatus(0); // Waiting for payment
            booking.setExtraPay(0);

            // Calculate price
            int totalDays = bookingService.calculateTotalDays(request.getCheckInDate(), request.getCheckOutDate());
            int totalPrice = bookingService.calculateTotalPrice(room, totalDays, request.isBreakfast());
            booking.setTotalPrice(totalPrice);

            AccommodationBooking savedBooking = bookingService.createBooking(booking);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.CREATED.value(),
                            "Booking created successfully",
                            savedBooking
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error creating booking: " + e.getMessage(),
                            null
                    ));
        }
    }

    // PUT confirm payment
    @PutMapping("/confirm/{id}")
    public ResponseEntity<BaseResponseDTO<AccommodationBooking>> confirmPayment(@PathVariable String id) {
        try {
            Optional<AccommodationBooking> bookingOpt = bookingService.getBookingById(id);

            if (bookingOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Booking not found",
                                null
                        ));
            }

            AccommodationBooking booking = bookingOpt.get();

            if (booking.getStatus() != 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Only bookings with status 0 can be confirmed",
                                null
                        ));
            }

            // Confirm booking and update property income
            AccommodationBooking updatedBooking = bookingService.payBooking(id);

            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Payment confirmed successfully",
                    updatedBooking
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error confirming payment: " + e.getMessage(),
                            null
                    ));
        }
    }

    // PUT cancel booking
    @PutMapping("/cancel/{id}")
    public ResponseEntity<BaseResponseDTO<AccommodationBooking>> cancelBooking(@PathVariable String id) {
        try {
            Optional<AccommodationBooking> bookingOpt = bookingService.getBookingById(id);

            if (bookingOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Booking not found",
                                null
                        ));
            }

            AccommodationBooking booking = bookingOpt.get();

            if (booking.getStatus() == 2 || booking.getStatus() == 3 || booking.getStatus() == 4) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Cannot cancel booking with current status",
                                null
                        ));
            }

            // Cancel booking (will handle refund if needed)
            AccommodationBooking updatedBooking = bookingService.cancelBooking(id);

            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Booking cancelled successfully",
                    updatedBooking
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error cancelling booking: " + e.getMessage(),
                            null
                    ));
        }
    }

    // PUT complete booking
    @PutMapping("/complete/{id}")
    public ResponseEntity<BaseResponseDTO<AccommodationBooking>> completeBooking(@PathVariable String id) {
        try {
            Optional<AccommodationBooking> bookingOpt = bookingService.getBookingById(id);

            if (bookingOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Booking not found",
                                null
                        ));
            }

            AccommodationBooking booking = bookingOpt.get();

            if (booking.getStatus() != 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Only confirmed bookings can be completed",
                                null
                        ));
            }

            booking.setStatus(4); // Done
            AccommodationBooking updatedBooking = bookingService.updateBookingDetails(booking);

            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Booking completed successfully",
                    updatedBooking
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error completing booking: " + e.getMessage(),
                            null
                    ));
        }
    }

    // GET count bookings
    @GetMapping("/count")
    public ResponseEntity<BaseResponseDTO<Long>> countBookings() {
        long count = bookingService.countAllBookings();
        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                count
        ));
    }
}
