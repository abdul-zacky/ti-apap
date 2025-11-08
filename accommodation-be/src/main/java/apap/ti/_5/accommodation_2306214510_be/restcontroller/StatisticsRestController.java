package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.BaseResponseDTO;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.service.AccommodationBookingService;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsRestController {

    private final PropertyService propertyService;
    private final AccommodationBookingService bookingService;

    public StatisticsRestController(PropertyService propertyService,
                                    AccommodationBookingService bookingService) {
        this.propertyService = propertyService;
        this.bookingService = bookingService;
    }

    // GET properties by type (for pie chart)
    @GetMapping("/properties-by-type")
    public ResponseEntity<BaseResponseDTO<Map<String, Integer>>> getPropertiesByType() {
        List<Property> allProperties = propertyService.getAllProperties();

        Map<String, Integer> typeCount = new HashMap<>();
        typeCount.put("Hotel", 0);
        typeCount.put("Villa", 0);
        typeCount.put("Apartment", 0);

        for (Property property : allProperties) {
            switch (property.getType()) {
                case 1:
                    typeCount.put("Hotel", typeCount.get("Hotel") + 1);
                    break;
                case 2:
                    typeCount.put("Villa", typeCount.get("Villa") + 1);
                    break;
                case 3:
                    typeCount.put("Apartment", typeCount.get("Apartment") + 1);
                    break;
            }
        }

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                typeCount
        ));
    }

    // GET bookings by status (for bar chart)
    @GetMapping("/bookings-by-status")
    public ResponseEntity<BaseResponseDTO<Map<String, Long>>> getBookingsByStatus() {
        Map<String, Long> statusCount = new LinkedHashMap<>();
        statusCount.put("Waiting", (long) bookingService.getBookingsByStatus(0).size());
        statusCount.put("Confirmed", (long) bookingService.getBookingsByStatus(1).size());
        statusCount.put("Cancelled", (long) bookingService.getBookingsByStatus(2).size());
        statusCount.put("Refund", (long) bookingService.getBookingsByStatus(3).size());
        statusCount.put("Done", (long) bookingService.getBookingsByStatus(4).size());

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                statusCount
        ));
    }

    // GET top 5 properties by income (for bar chart)
    @GetMapping("/top-properties-by-income")
    public ResponseEntity<BaseResponseDTO<Map<String, Integer>>> getTopPropertiesByIncome() {
        List<Property> allProperties = propertyService.getAllProperties();

        // Sort by income descending and take top 5
        allProperties.sort((p1, p2) -> Integer.compare(p2.getIncome(), p1.getIncome()));

        Map<String, Integer> topProperties = new LinkedHashMap<>();
        int limit = Math.min(5, allProperties.size());

        for (int i = 0; i < limit; i++) {
            Property property = allProperties.get(i);
            topProperties.put(property.getPropertyName(), property.getIncome());
        }

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                topProperties
        ));
    }

    // GET properties by province (for geographic visualization)
    @GetMapping("/properties-by-province")
    public ResponseEntity<BaseResponseDTO<Map<String, Integer>>> getPropertiesByProvince() {
        List<Property> allProperties = propertyService.getAllProperties();

        Map<String, Integer> provinceCount = new HashMap<>();

        for (Property property : allProperties) {
            String province = property.getProvince();
            provinceCount.put(province, provinceCount.getOrDefault(province, 0) + 1);
        }

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                provinceCount
        ));
    }

    // GET overall statistics summary
    @GetMapping("/summary")
    public ResponseEntity<BaseResponseDTO<Map<String, Object>>> getStatisticsSummary() {
        Map<String, Object> summary = new HashMap<>();

        // Total counts
        summary.put("totalProperties", propertyService.countProperties());
        summary.put("totalBookings", bookingService.countAllBookings());

        // Active properties
        long activeProperties = propertyService.getAllProperties().stream()
                .filter(p -> p.getActiveStatus() == 1)
                .count();
        summary.put("activeProperties", activeProperties);

        // Total income from all properties
        int totalIncome = propertyService.getAllProperties().stream()
                .mapToInt(Property::getIncome)
                .sum();
        summary.put("totalIncome", totalIncome);

        // Confirmed bookings count
        long confirmedBookings = bookingService.getBookingsByStatus(1).size();
        summary.put("confirmedBookings", confirmedBookings);

        return ResponseEntity.ok(new BaseResponseDTO<>(
                HttpStatus.OK.value(),
                "Success",
                summary
        ));
    }
}
