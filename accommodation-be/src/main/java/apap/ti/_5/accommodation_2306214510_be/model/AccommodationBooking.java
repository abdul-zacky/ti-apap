package apap.ti._5.accommodation_2306214510_be.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accommodation_booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationBooking {

    @Id
    @Column(name = "booking_id", nullable = false, unique = true)
    private String bookingID;

    @Column(name = "check_in_date", nullable = false)
    private LocalDateTime checkInDate;

    @Column(name = "check_out_date", nullable = false)
    private LocalDateTime checkOutDate;

    @Column(name = "total_days", nullable = false)
    private int totalDays;

    @Column(name = "total_price", nullable = false)
    private int totalPrice;

    @Column(name = "status", nullable = false)
    private int status; // 0: Waiting for Payment, 1: Payment Confirmed, 2: Cancelled, 3: Request Refund, 4: Done

    @Column(name = "customer_id", nullable = false)
    private UUID customerID;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_email", nullable = false)
    private String customerEmail;

    @Column(name = "customer_phone", nullable = false)
    private String customerPhone;

    @Column(name = "is_breakfast", nullable = false)
    private boolean isBreakfast;

    @Column(name = "refund", nullable = false)
    private int refund;

    @Column(name = "extra_pay", nullable = false)
    private int extraPay;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = false)
    private Room room;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        if (status == 0) {
            status = 0; // Default to Waiting for Payment
        }
        if (refund == 0) {
            refund = 0;
        }
        if (extraPay == 0) {
            extraPay = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
