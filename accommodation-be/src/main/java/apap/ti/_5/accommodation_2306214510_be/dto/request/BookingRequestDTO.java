package apap.ti._5.accommodation_2306214510_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    private String roomID;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private UUID customerID;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private boolean isBreakfast;
    private int capacity;
}
