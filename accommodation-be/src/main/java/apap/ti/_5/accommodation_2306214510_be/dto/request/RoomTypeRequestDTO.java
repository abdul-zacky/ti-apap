package apap.ti._5.accommodation_2306214510_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeRequestDTO {
    private String name;
    private int price;
    private String description;
    private int capacity;
    private String facility;
    private int floor;
    private int unit; // Number of rooms to create
}
