package apap.ti._5.accommodation_2306214510_be.schedule;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.service.AccommodationBookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BookingStatusScheduler {

    @Autowired
    private AccommodationBookingService bookingService;

    /**
     * Auto-update booking status every hour (3600000 ms)
     * Logic:
     * 1. If current time >= checkInDate AND status == 1 (Confirmed) -> Change to 4 (Done)
     * 2. If current time >= checkInDate AND status == 0 (Waiting) -> Change to 2 (Cancelled)
     * 3. If current time >= checkInDate AND status == 3 (Refund) -> Change to 2 (Cancelled)
     */
    @Scheduled(fixedRate = 3600000) // Run every 1 hour
    public void updateBookingStatusAutomatically() {
        log.info("=== Starting automatic booking status update ===");
        
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            List<AccommodationBooking> allBookings = bookingService.getAllBookings();
            
            int updatedCount = 0;
            
            for (AccommodationBooking booking : allBookings) {
                // Only process if check-in date has reached or passed
                if (currentDateTime.isAfter(booking.getCheckInDate()) || 
                    currentDateTime.isEqual(booking.getCheckInDate())) {
                    
                    int newStatus = booking.getStatus();
                    String reason = "";
                    
                    // Rule 1: Status 1 (Confirmed) -> 4 (Done)
                    if (booking.getStatus() == 1) {
                        newStatus = 4;
                        reason = "Auto-changed from Confirmed to Done (check-in date reached)";
                    }
                    // Rule 2: Status 0 (Waiting for Payment) -> 2 (Cancelled)
                    else if (booking.getStatus() == 0) {
                        newStatus = 2;
                        reason = "Auto-cancelled from Waiting (check-in date reached without payment)";
                    }
                    // Rule 3: Status 3 (Refund Request) -> 2 (Cancelled)
                    else if (booking.getStatus() == 3) {
                        newStatus = 2;
                        reason = "Auto-cancelled from Refund Request (check-in date reached)";
                    }
                    
                    // Update if status changed
                    if (newStatus != booking.getStatus()) {
                        bookingService.updateBookingStatus(booking.getBookingID(), newStatus);
                        updatedCount++;
                        log.info("Updated booking {} - {}. New status: {}", 
                            booking.getBookingID(), reason, newStatus);
                    }
                }
            }
            
            log.info("=== Completed automatic booking status update. {} bookings updated ===", updatedCount);
        } catch (Exception e) {
            log.error("Error during automatic booking status update", e);
        }
    }
}
