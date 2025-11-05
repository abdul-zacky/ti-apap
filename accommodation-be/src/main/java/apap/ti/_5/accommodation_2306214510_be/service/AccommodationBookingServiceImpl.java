package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.repository.AccommodationBookingRepository;
import apap.ti._5.accommodation_2306214510_be.repository.PropertyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class AccommodationBookingServiceImpl implements AccommodationBookingService {

    private static final int BREAKFAST_PRICE_PER_DAY = 50000;
    private final AccommodationBookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;

    // Constructor Dependency Injection
    public AccommodationBookingServiceImpl(AccommodationBookingRepository bookingRepository,
                                           PropertyRepository propertyRepository) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public AccommodationBooking createBooking(AccommodationBooking booking) {
        booking.setStatus(0); // Waiting for payment
        booking.setCreatedDate(LocalDateTime.now());
        booking.setUpdatedDate(LocalDateTime.now());
        booking.setRefund(0);
        booking.setExtraPay(0);

        // Calculate total days
        int totalDays = calculateTotalDays(booking.getCheckInDate(), booking.getCheckOutDate());
        booking.setTotalDays(totalDays);

        // Calculate total price
        int totalPrice = calculateTotalPrice(booking.getRoom(), totalDays, booking.isBreakfast());
        booking.setTotalPrice(totalPrice);

        return bookingRepository.save(booking);
    }

    @Override
    public List<AccommodationBooking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingID();
    }

    @Override
    public Optional<AccommodationBooking> getBookingById(String id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<AccommodationBooking> getBookingsByStatus(int status) {
        return bookingRepository.findByStatusOrderByBookingID(status);
    }

    @Override
    public List<AccommodationBooking> getBookingsByCustomer(UUID customerId) {
        return bookingRepository.findByCustomerIDOrderByBookingID(customerId);
    }

    @Override
    public List<AccommodationBooking> searchBookings(String searchTerm, Integer status) {
        if (status != null) {
            return bookingRepository.searchBookingsByStatus(searchTerm, status);
        }
        return bookingRepository.searchBookings(searchTerm);
    }

    @Override
    public long countAllBookings() {
        return bookingRepository.count();
    }

    @Override
    public AccommodationBooking updateBookingDetails(AccommodationBooking newBooking) {
        Optional<AccommodationBooking> oldBookingOpt = bookingRepository.findById(newBooking.getBookingID());
        if (oldBookingOpt.isEmpty()) {
            return null;
        }

        AccommodationBooking oldBooking = oldBookingOpt.get();

        // Calculate new total days and price
        int newTotalDays = calculateTotalDays(newBooking.getCheckInDate(), newBooking.getCheckOutDate());
        int newTotalPrice = calculateTotalPrice(newBooking.getRoom(), newTotalDays, newBooking.isBreakfast());

        newBooking.setTotalDays(newTotalDays);
        newBooking.setTotalPrice(newTotalPrice);
        newBooking.setUpdatedDate(LocalDateTime.now());

        // Calculate price difference
        int priceDifference = newTotalPrice - oldBooking.getTotalPrice();

        if (priceDifference > 0) {
            // Price increased
            handlePriceIncrease(newBooking, priceDifference);
        } else if (priceDifference < 0) {
            // Price decreased
            handlePriceDecrease(newBooking, Math.abs(priceDifference));
        }

        return bookingRepository.save(newBooking);
    }

    @Override
    public AccommodationBooking updateBookingStatus(String bookingId, int newStatus) {
        Optional<AccommodationBooking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            AccommodationBooking booking = bookingOpt.get();
            booking.setStatus(newStatus);
            booking.setUpdatedDate(LocalDateTime.now());
            return bookingRepository.save(booking);
        }
        return null;
    }

    @Override
    public AccommodationBooking payBooking(String bookingId) {
        Optional<AccommodationBooking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return null;
        }

        AccommodationBooking booking = bookingOpt.get();
        String propertyId = booking.getRoom().getRoomType().getProperty().getPropertyID();

        // If has extra pay, add it to income and clear extra pay
        if (booking.getExtraPay() > 0) {
            updatePropertyIncome(propertyId, booking.getExtraPay(), true);
            booking.setExtraPay(0);
        } else {
            // Normal payment
            updatePropertyIncome(propertyId, booking.getTotalPrice(), true);
        }

        booking.setStatus(1); // Payment confirmed
        booking.setUpdatedDate(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    @Override
    public AccommodationBooking cancelBooking(String bookingId) {
        Optional<AccommodationBooking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return null;
        }

        AccommodationBooking booking = bookingOpt.get();
        String propertyId = booking.getRoom().getRoomType().getProperty().getPropertyID();
        int currentStatus = booking.getStatus();

        // Handle income based on current status
        if (currentStatus == 0 && booking.getExtraPay() > 0) {
            // Waiting for payment with extra pay
            int amountToReturn = booking.getTotalPrice() - booking.getExtraPay();
            updatePropertyIncome(propertyId, amountToReturn, false);
        } else if (currentStatus == 1) {
            // Payment confirmed
            updatePropertyIncome(propertyId, booking.getTotalPrice(), false);
        } else if (currentStatus == 3) {
            // Request refund
            int amountToReturn = booking.getTotalPrice() + booking.getRefund();
            updatePropertyIncome(propertyId, amountToReturn, false);
        }

        booking.setStatus(2); // Cancelled
        booking.setUpdatedDate(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    @Override
    public AccommodationBooking refundBooking(String bookingId) {
        Optional<AccommodationBooking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return null;
        }

        AccommodationBooking booking = bookingOpt.get();
        String propertyId = booking.getRoom().getRoomType().getProperty().getPropertyID();

        // Reduce property income by refund amount
        updatePropertyIncome(propertyId, booking.getRefund(), false);

        booking.setRefund(0);
        booking.setStatus(1); // Back to payment confirmed
        booking.setUpdatedDate(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    @Override
    public void processCheckInUpdates() {
        LocalDateTime now = LocalDateTime.now();

        // Mark payment confirmed bookings as done
        List<AccommodationBooking> bookingsToMarkDone = bookingRepository.findBookingsToMarkAsDone(now);
        for (AccommodationBooking booking : bookingsToMarkDone) {
            // Handle refund if exists
            if (booking.getStatus() == 3 && booking.getRefund() > 0) {
                String propertyId = booking.getRoom().getRoomType().getProperty().getPropertyID();
                updatePropertyIncome(propertyId, booking.getRefund(), false);
                booking.setRefund(0);
            }
            booking.setStatus(4); // Done
            booking.setUpdatedDate(now);
            bookingRepository.save(booking);
        }

        // Cancel waiting/refund bookings
        List<AccommodationBooking> bookingsToCancel = bookingRepository.findBookingsToCancel(now);
        for (AccommodationBooking booking : bookingsToCancel) {
            String propertyId = booking.getRoom().getRoomType().getProperty().getPropertyID();

            if (booking.getStatus() == 0 && booking.getExtraPay() > 0) {
                int amountToReturn = booking.getTotalPrice() - booking.getExtraPay();
                updatePropertyIncome(propertyId, amountToReturn, false);
            } else if (booking.getStatus() == 3) {
                int amountToReturn = booking.getTotalPrice() + booking.getRefund();
                updatePropertyIncome(propertyId, amountToReturn, false);
            }

            booking.setStatus(2); // Cancelled
            booking.setUpdatedDate(now);
            bookingRepository.save(booking);
        }
    }

    @Override
    public String generateBookingId(String roomId, LocalDateTime bookingTime) {
        // Extract last 7 digits from room ID
        String[] parts = roomId.split("-");
        String last7 = parts.length >= 2 ? parts[parts.length - 2] + "-" + parts[parts.length - 1] : roomId.substring(Math.max(0, roomId.length() - 7));

        // Format: BOOK-004-101-2025-10-24-10:38:12
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String timeStr = bookingTime.format(formatter);

        return "BOOK-" + last7 + "-" + timeStr;
    }

    @Override
    public int calculateTotalDays(LocalDateTime checkIn, LocalDateTime checkOut) {
        return (int) ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
    }

    @Override
    public int calculateTotalPrice(Room room, int totalDays, boolean isBreakfast) {
        int basePrice = room.getRoomType().getPrice() * totalDays;
        int breakfastPrice = isBreakfast ? BREAKFAST_PRICE_PER_DAY * totalDays : 0;
        return basePrice + breakfastPrice;
    }

    @Override
    public boolean hasConflictingBooking(String roomId, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<AccommodationBooking> conflictingBookings = bookingRepository.findConflictingBookings(roomId, checkIn, checkOut);
        return !conflictingBookings.isEmpty();
    }

    @Override
    public boolean canUpdateBooking(AccommodationBooking booking) {
        return (booking.getStatus() == 0 && booking.getExtraPay() == 0) || booking.getStatus() == 1;
    }

    @Override
    public boolean canCancelBooking(AccommodationBooking booking) {
        return booking.getStatus() == 0 || booking.getStatus() == 1 || booking.getStatus() == 3;
    }

    @Override
    public int calculatePriceChange(AccommodationBooking oldBooking, AccommodationBooking newBooking) {
        return newBooking.getTotalPrice() - oldBooking.getTotalPrice();
    }

    @Override
    public void handlePriceIncrease(AccommodationBooking booking, int priceDifference) {
        booking.setExtraPay(priceDifference);
        booking.setStatus(0); // Back to waiting for payment
    }

    @Override
    public void handlePriceDecrease(AccommodationBooking booking, int priceDifference) {
        booking.setRefund(priceDifference);
        booking.setStatus(3); // Request refund
    }

    @Override
    public void updatePropertyIncome(String propertyId, int amount, boolean isIncrease) {
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        if (propertyOpt.isPresent()) {
            Property property = propertyOpt.get();
            int currentIncome = property.getIncome();

            if (isIncrease) {
                property.setIncome(currentIncome + amount);
            } else {
                property.setIncome(currentIncome - amount);
            }

            property.setUpdatedDate(LocalDateTime.now());
            propertyRepository.save(property);
        }
    }

    @Override
    public Map<String, Integer> getBookingStatistics(int month, int year) {
        Map<String, Integer> statistics = new HashMap<>();

        // Get all properties
        List<Property> properties = propertyRepository.findAllByOrderByUpdatedDateDesc();

        for (Property property : properties) {
            List<AccommodationBooking> doneBookings = bookingRepository
                    .findDoneBookingsByPropertyAndMonth(property.getPropertyID(), month, year);

            int totalIncome = doneBookings.stream()
                    .mapToInt(AccommodationBooking::getTotalPrice)
                    .sum();

            if (totalIncome > 0) {
                statistics.put(property.getPropertyName(), totalIncome);
            }
        }

        return statistics;
    }

    @Override
    public List<AccommodationBooking> getDoneBookingsByMonth(int month, int year) {
        List<Property> properties = propertyRepository.findAll();
        List<AccommodationBooking> allDoneBookings = new ArrayList<>();

        for (Property property : properties) {
            allDoneBookings.addAll(bookingRepository.findDoneBookingsByPropertyAndMonth(
                    property.getPropertyID(), month, year));
        }

        return allDoneBookings;
    }
}
