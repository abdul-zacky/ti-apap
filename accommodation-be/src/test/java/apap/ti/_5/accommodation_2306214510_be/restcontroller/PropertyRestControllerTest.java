package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import apap.ti._5.accommodation_2306214510_be.service.RoomTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyRestControllerTest {

    @Mock
    private PropertyService propertyService;

    @Mock
    private RoomTypeService roomTypeService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private PropertyRestController propertyRestController;

    @Test
    void testGetAllProperties_NoFilters() {
        Property property = new Property();
        property.setPropertyID("HOT-abc123-001");
        
        when(propertyService.searchProperties(null, null, null))
            .thenReturn(Arrays.asList(property));
        
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService).searchProperties(null, null, null);
    }

    @Test
    void testGetAllProperties_WithNameFilter() {
        when(propertyService.searchProperties("Hotel", null, null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties("Hotel", null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService).searchProperties("Hotel", null, null);
    }

    @Test
    void testGetAllProperties_WithTypeFilter() {
        when(propertyService.searchProperties(null, 1, null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, 1, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService).searchProperties(null, 1, null);
    }

    @Test
    void testGetAllProperties_WithStatusFilter() {
        when(propertyService.searchProperties(null, null, 1))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, null, 1);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService).searchProperties(null, null, 1);
    }

    @Test
    void testGetAllProperties_WithAllFilters() {
        when(propertyService.searchProperties("Grand Hotel", 1, 1))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties("Grand Hotel", 1, 1);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertyById_Found() {
        Property property = new Property();
        property.setPropertyID("HOT-abc123-001");
        
        when(propertyService.getPropertyById("HOT-abc123-001"))
            .thenReturn(Optional.of(property));
        
        ResponseEntity response = propertyRestController.getPropertyById("HOT-abc123-001");
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService).getPropertyById("HOT-abc123-001");
    }

    @Test
    void testGetPropertyById_NotFound() {
        when(propertyService.getPropertyById("INVALID-ID"))
            .thenReturn(Optional.empty());
        
        ResponseEntity response = propertyRestController.getPropertyById("INVALID-ID");
        
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        verify(propertyService).getPropertyById("INVALID-ID");
    }

    @Test
    void testGetAllProperties_EmptyResult() {
        when(propertyService.searchProperties(null, null, null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_MultipleProperties() {
        Property p1 = new Property();
        Property p2 = new Property();
        Property p3 = new Property();
        
        when(propertyService.searchProperties(null, null, null))
            .thenReturn(Arrays.asList(p1, p2, p3));
        
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_WithNameAndType() {
        when(propertyService.searchProperties("Hotel", 1, null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties("Hotel", 1, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_WithNameAndStatus() {
        when(propertyService.searchProperties("Grand", null, 1))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties("Grand", null, 1);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_WithTypeAndStatus() {
        when(propertyService.searchProperties(null, 2, 1))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, 2, 1);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_HotelType() {
        when(propertyService.searchProperties(null, 1, null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, 1, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_VillaType() {
        when(propertyService.searchProperties(null, 2, null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, 2, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_ApartmentType() {
        when(propertyService.searchProperties(null, 3, null))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, 3, null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_ActiveStatus() {
        when(propertyService.searchProperties(null, null, 1))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, null, 1);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_InactiveStatus() {
        when(propertyService.searchProperties(null, null, 0))
            .thenReturn(new ArrayList<>());
        
        ResponseEntity response = propertyRestController.getAllProperties(null, null, 0);
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
