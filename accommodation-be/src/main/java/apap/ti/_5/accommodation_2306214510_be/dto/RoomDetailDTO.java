package apap.ti._5.accommodation_2306214510_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailDTO {
    private String roomID;
    private String name;
    private int availabilityStatus;
    private int activeRoom;
    private String roomTypeID;
}
