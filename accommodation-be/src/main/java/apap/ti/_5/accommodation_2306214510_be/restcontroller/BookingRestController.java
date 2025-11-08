package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.BaseResponseDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.BookingRequestDTO;
import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.RoomRepository;
import apap.ti._5.accommodation_2306214510_be.service.AccommodationBookingService;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "*")
@Slf4j
public class BookingRestController {

    private final AccommodationBookingService bookingService;
    private final RoomService roomService;
    private final PropertyService propertyService;
    private final RoomRepository roomRepository;
    private final AccommodationBookingRepository bookingRepository;

    public BookingRestController(AccommodationBookingService bookingService,
                                 RoomService roomService,
                                 PropertyService propertyService,
                                 RoomRepository roomRepository,
                                 AccommodationBookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.roomService = roomService;
        this.propertyService = propertyService;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    // GET all bookings with optional filters
    @GetMapping("")
    public ResponseEntity<BaseResponseDTO<List<AccommodationBooking>>> getAllBookings(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Integer status) {

        List<AccommodationBooking> bookings;

        // If both filters are provided
        if (searchTerm != null && status != null) {
            bookings = bookingService.searchBookings(searchTerm, status);
        }
        // If only status filter is provided
        else if (status != null) {
            bookings = bookingService.getBookingsByStatus(status);
        }
        // If only searchTerm filter is provided
        else if (searchTerm != null) {
            bookings = bookingService.searchBookings(searchTerm, null);
        }
        // If no filters are provided
        else {
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
            log.info("=== CREATE BOOKING ===");
            log.info("Room ID: {}", request.getRoomID());
            log.info("Check-In: {}", request.getCheckInDate());
            log.info("Check-Out: {}", request.getCheckOutDate());
            log.info("Customer: {}", request.getCustomerName());
            
            // Validate room exists
            Optional<Room> roomOpt = roomService.getRoomById(request.getRoomID());
            if (roomOpt.isEmpty()) {
                log.warn("Room not found: {}", request.getRoomID());
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
                log.warn("Invalid dates: CheckOut before CheckIn");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Check-out date must be after check-in date",
                                null
                        ));
            }

            // Check room availability
            log.info("Checking room availability...");
            boolean isAvailable = roomService.isRoomAvailable(request.getRoomID(), request.getCheckInDate(), request.getCheckOutDate());
            log.info("Room available: {}", isAvailable);
            
            if (!isAvailable) {
                log.warn("Room is not available for requested dates");
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
            booking.setRefund(0);

            // Calculate price
            int totalDays = bookingService.calculateTotalDays(request.getCheckInDate(), request.getCheckOutDate());
            int totalPrice = bookingService.calculateTotalPrice(room, totalDays, request.isBreakfast());
            booking.setTotalPrice(totalPrice);
            booking.setTotalDays(totalDays);

            log.info("Saving booking with ID: {} Status: 0 (Waiting) TotalDays: {} TotalPrice: {}", bookingId, totalDays, totalPrice);
            AccommodationBooking savedBooking = bookingService.createBooking(booking);
            
            log.info("Booking saved successfully!");
            log.info("Saved Booking ID: {} Status: {} CheckIn: {} CheckOut: {}", 
                savedBooking.getBookingID(), savedBooking.getStatus(), savedBooking.getCheckInDate(), savedBooking.getCheckOutDate());

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
            log.info("=== CONFIRM BOOKING PAYMENT ===");
            log.info("Booking ID: {}", id);
            
            Optional<AccommodationBooking> bookingOpt = bookingService.getBookingById(id);

            if (bookingOpt.isEmpty()) {
                log.warn("Booking not found: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Booking not found",
                                null
                        ));
            }

            AccommodationBooking booking = bookingOpt.get();
            log.info("Current booking status: {}", booking.getStatus());
            log.info("Room ID: {} CheckIn: {} CheckOut: {}", booking.getRoom().getRoomID(), booking.getCheckInDate(), booking.getCheckOutDate());

            if (booking.getStatus() != 0) {
                log.warn("Booking status is not 0, current status: {}", booking.getStatus());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Only bookings with status 0 can be confirmed",
                                null
                        ));
            }

            // Confirm booking and update property income
            log.info("Confirming booking...");
            AccommodationBooking updatedBooking = bookingService.payBooking(id);
            log.info("Booking confirmed! New status: {}", updatedBooking.getStatus());

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

    // PUT process refund
    @PutMapping("/refund/{id}")
    public ResponseEntity<BaseResponseDTO<AccommodationBooking>> processRefund(@PathVariable String id) {
        try {
            log.info("💰 PUT /booking/refund/{} - Processing refund", id);
            
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

            // Only allow refund for status 3 (Request Refund) with negative extraPay
            if (booking.getStatus() != 3) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Only bookings with status 'Request Refund' can be processed",
                                null
                        ));
            }

            if (booking.getExtraPay() >= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "No refund amount found for this booking",
                                null
                        ));
            }

            // Get refund amount (negative extraPay)
            int refundAmount = Math.abs(booking.getExtraPay());
            String propertyId = booking.getRoom().getRoomType().getProperty().getPropertyID();
            
            log.info("💰 Refunding Rp {} to customer, reducing property {} income", refundAmount, propertyId);
            
            // Reduce property income
            bookingService.updatePropertyIncome(propertyId, refundAmount, false);
            
            // Update booking status and clear extraPay
            booking.setStatus(1); // Back to Confirmed
            booking.setExtraPay(0); // Clear refund amount
            booking.setUpdatedDate(LocalDateTime.now());
            
            AccommodationBooking updatedBooking = bookingRepository.save(booking);
            
            log.info("✅ Refund processed successfully");

            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Refund processed successfully - Rp " + refundAmount,
                    updatedBooking
            ));

        } catch (Exception e) {
            log.error("❌ Error processing refund: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error processing refund: " + e.getMessage(),
                            null
                    ));
        }
    }

    // PUT update booking details
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponseDTO<AccommodationBooking>> updateBooking(
            @PathVariable String id,
            @RequestBody BookingRequestDTO request) {
        try {
            log.info("🔄 PUT /booking/update/{} - Updating booking", id);
            
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
            
            // Only allow update for confirmed bookings (status = 1)
            if (booking.getStatus() != 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.BAD_REQUEST.value(),
                                "Only confirmed bookings can be updated",
                                null
                        ));
            }

            // Store old total price for refund/extra pay calculation
            int oldTotalPrice = booking.getTotalPrice();
            
            // Get new room
            Optional<Room> newRoomOpt = roomRepository.findById(request.getRoomID());
            if (newRoomOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.NOT_FOUND.value(),
                                "Room not found",
                                null
                        ));
            }
            
            Room newRoom = newRoomOpt.get();
            
            // Calculate new total price
            LocalDateTime checkIn = request.getCheckInDate();
            LocalDateTime checkOut = request.getCheckOutDate();
            long days = java.time.Duration.between(checkIn, checkOut).toDays();
            
            int newRoomPrice = newRoom.getRoomType().getPrice();
            int newTotalPrice = newRoomPrice * (int) days;
            
            if (request.isBreakfast()) {
                newTotalPrice += 50000 * (int) days;
            }
            
            // Calculate refund or extra pay
            int priceDifference = newTotalPrice - oldTotalPrice;
            
            log.info("💰 Price calculation - Old: {}, New: {}, Difference: {}", 
                    oldTotalPrice, newTotalPrice, priceDifference);
            
            // Update booking fields
            booking.setRoom(newRoom);
            booking.setCheckInDate(checkIn);
            booking.setCheckOutDate(checkOut);
            booking.setCustomerID(request.getCustomerID());
            booking.setCustomerName(request.getCustomerName());
            booking.setCustomerEmail(request.getCustomerEmail());
            booking.setCustomerPhone(request.getCustomerPhone());
            booking.setBreakfast(request.isBreakfast());
            booking.setCapacity(request.getCapacity());
            booking.setTotalPrice(newTotalPrice);
            booking.setExtraPay(priceDifference);
            booking.setUpdatedDate(LocalDateTime.now());
            
            // Update status based on price difference
            if (priceDifference > 0) {
                // Extra payment needed - change to waiting for payment
                booking.setStatus(0);
            } else if (priceDifference < 0) {
                // Refund needed - change to request refund
                booking.setStatus(3);
            }
            // If priceDifference == 0, status remains unchanged (1 = Confirmed)
            
            // Save directly to avoid service recalculation
            AccommodationBooking updatedBooking = bookingRepository.save(booking);
            
            String message = "Booking updated successfully";
            if (priceDifference > 0) {
                message += " - Extra payment required: Rp " + priceDifference;
            } else if (priceDifference < 0) {
                message += " - Refund amount: Rp " + Math.abs(priceDifference);
            }
            
            log.info("✅ Booking updated - {}", message);

            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    message,
                    updatedBooking
            ));

        } catch (Exception e) {
            log.error("❌ Error updating booking: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error updating booking: " + e.getMessage(),
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

    // GET booking chart (income by property - shows actual property income from database)
    @GetMapping("/chart")
    public ResponseEntity<BaseResponseDTO<?>> getBookingChart(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        
        try {
            log.info("📊 GET /booking/chart - month: {}, year: {}", month, year);
            
            // Get all properties and their current income from database
            List<Property> allProperties = propertyService.getAllProperties();
            
            // Map to store property name -> income
            java.util.Map<String, Integer> propertyIncomeMap = new java.util.LinkedHashMap<>();
            
            for (Property property : allProperties) {
                // Only show properties with income > 0
                if (property.getIncome() > 0) {
                    propertyIncomeMap.put(property.getPropertyName(), property.getIncome());
                }
            }
            
            // Sort by income descending
            var sortedMap = propertyIncomeMap.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .collect(java.util.stream.Collectors.toMap(
                    java.util.Map.Entry::getKey,
                    java.util.Map.Entry::getValue,
                    (e1, e2) -> e1,
                    java.util.LinkedHashMap::new
                ));
            
            log.info("✅ Chart data prepared with {} properties", sortedMap.size());
            
            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Success",
                    sortedMap
            ));
            
        } catch (Exception e) {
            log.error("❌ Error getting booking chart: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Error getting booking chart: " + e.getMessage(),
                            null
                    ));
        }
    }
}
