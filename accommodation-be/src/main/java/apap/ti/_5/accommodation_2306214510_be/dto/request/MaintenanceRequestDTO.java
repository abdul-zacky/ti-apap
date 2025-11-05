package apap.ti._5.accommodation_2306214510_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestDTO {
    private String roomID;
    private LocalDateTime maintenanceStart;
    private LocalDateTime maintenanceEnd;
}
