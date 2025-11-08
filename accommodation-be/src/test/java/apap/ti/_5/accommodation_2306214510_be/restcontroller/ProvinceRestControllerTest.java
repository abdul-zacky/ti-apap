package apap.ti._5.accommodation_2306214510_be.restcontroller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProvinceRestControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProvinceRestController provinceRestController;

    @Test
    void testGetAllProvinces_Success() {
        Map<String, Object> province1 = new HashMap<>();
        province1.put("code", "11");
        province1.put("name", "ACEH");
        
        Map<String, Object> province2 = new HashMap<>();
        province2.put("code", "12");
        province2.put("name", "SUMATERA UTARA");
        
        List<Map<String, Object>> provinceList = Arrays.asList(province1, province2);
        
        Map<String, List<Map<String, Object>>> apiResponse = new HashMap<>();
        apiResponse.put("data", provinceList);
        
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenReturn(apiResponse);
        
        ResponseEntity response = provinceRestController.getAllProvinces();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProvinces_NullResponse() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenReturn(null);
        
        ResponseEntity response = provinceRestController.getAllProvinces();
        
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProvinces_NoDataKey() {
        Map<String, List<Map<String, Object>>> apiResponse = new HashMap<>();
        
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenReturn(apiResponse);
        
        ResponseEntity response = provinceRestController.getAllProvinces();
        
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProvinces_Exception() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenThrow(new RuntimeException("API Error"));
        
        ResponseEntity response = provinceRestController.getAllProvinces();
        
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProvinces_EmptyList() {
        List<Map<String, Object>> emptyList = new ArrayList<>();
        
        Map<String, List<Map<String, Object>>> apiResponse = new HashMap<>();
        apiResponse.put("data", emptyList);
        
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
            .thenReturn(apiResponse);
        
        ResponseEntity response = provinceRestController.getAllProvinces();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
