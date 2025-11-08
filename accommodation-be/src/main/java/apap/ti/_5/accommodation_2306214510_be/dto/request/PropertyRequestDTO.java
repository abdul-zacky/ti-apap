package apap.ti._5.accommodation_2306214510_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyRequestDTO {
    private String propertyName;
    private int type; // 1: Hotel, 2: Villa, 3: Apartment
    private String province;
    private String address;
    private String description;
    private UUID ownerID;
    private String ownerName;
    private List<RoomTypeRequestDTO> roomTypes;
}
