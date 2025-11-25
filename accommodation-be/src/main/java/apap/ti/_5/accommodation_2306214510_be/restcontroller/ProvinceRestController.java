package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.BaseResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/province")
public class ProvinceRestController {

    private final RestTemplate restTemplate;

    public ProvinceRestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("")
    public ResponseEntity<BaseResponseDTO<List<Map<String, Object>>>> getAllProvinces() {
        try {
            String url = "https://wilayah.id/api/provinces.json";
            
            @SuppressWarnings("unchecked")
            Map<String, List<Map<String, Object>>> response = restTemplate.getForObject(url, Map.class);
            
            if (response == null || !response.containsKey("data")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new BaseResponseDTO<>(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Invalid response format from province API",
                                null
                        ));
            }

            List<Map<String, Object>> provinces = response.get("data");

            return ResponseEntity.ok(new BaseResponseDTO<>(
                    HttpStatus.OK.value(),
                    "Success",
                    provinces
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponseDTO<>(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Failed to fetch provinces: " + e.getMessage(),
                            null
                    ));
        }
    }
}
