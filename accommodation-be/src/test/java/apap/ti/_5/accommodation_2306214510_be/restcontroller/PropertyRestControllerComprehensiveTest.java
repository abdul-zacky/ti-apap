package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
import apap.ti._5.accommodation_2306214510_be.service.RoomService;
import apap.ti._5.accommodation_2306214510_be.service.RoomTypeService;
import org.junit.jupiter.api.BeforeEach;
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
class PropertyRestControllerComprehensiveTest {

    @Mock
    private PropertyService propertyService;

    @Mock
    private RoomTypeService roomTypeService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private PropertyRestController propertyRestController;

    private Property property;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyID("HOT-001");
        property.setPropertyName("Test Hotel");
        property.setType(1);
        property.setActiveStatus(1);
    }

    // Test all type combinations
    @Test
    void testGetAllProperties_Type1() {
        when(propertyService.searchProperties(null, 1, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 1, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_Type2() {
        when(propertyService.searchProperties(null, 2, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 2, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_Type3() {
        when(propertyService.searchProperties(null, 3, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 3, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test all status combinations
    @Test
    void testGetAllProperties_StatusActive() {
        when(propertyService.searchProperties(null, null, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, null, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_StatusInactive() {
        when(propertyService.searchProperties(null, null, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, null, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test name search variations
    @Test
    void testGetAllProperties_NameHotel() {
        when(propertyService.searchProperties("Hotel", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Hotel", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameVilla() {
        when(propertyService.searchProperties("Villa", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Villa", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameApartment() {
        when(propertyService.searchProperties("Apartment", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Apartment", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameGrand() {
        when(propertyService.searchProperties("Grand", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Grand", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameLuxury() {
        when(propertyService.searchProperties("Luxury", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Luxury", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test combined filters - Type + Status
    @Test
    void testGetAllProperties_Type1StatusActive() {
        when(propertyService.searchProperties(null, 1, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 1, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_Type1StatusInactive() {
        when(propertyService.searchProperties(null, 1, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 1, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_Type2StatusActive() {
        when(propertyService.searchProperties(null, 2, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 2, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_Type2StatusInactive() {
        when(propertyService.searchProperties(null, 2, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 2, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_Type3StatusActive() {
        when(propertyService.searchProperties(null, 3, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 3, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_Type3StatusInactive() {
        when(propertyService.searchProperties(null, 3, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, 3, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test combined filters - Name + Type
    @Test
    void testGetAllProperties_NameHotelType1() {
        when(propertyService.searchProperties("Hotel", 1, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Hotel", 1, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameVillaType2() {
        when(propertyService.searchProperties("Villa", 2, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Villa", 2, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameApartmentType3() {
        when(propertyService.searchProperties("Apartment", 3, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Apartment", 3, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test combined filters - Name + Status
    @Test
    void testGetAllProperties_NameHotelStatusActive() {
        when(propertyService.searchProperties("Hotel", null, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Hotel", null, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameHotelStatusInactive() {
        when(propertyService.searchProperties("Hotel", null, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Hotel", null, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameVillaStatusActive() {
        when(propertyService.searchProperties("Villa", null, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Villa", null, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameVillaStatusInactive() {
        when(propertyService.searchProperties("Villa", null, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Villa", null, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test all three filters combined
    @Test
    void testGetAllProperties_AllFilters_HotelType1Active() {
        when(propertyService.searchProperties("Hotel", 1, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Hotel", 1, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_AllFilters_HotelType1Inactive() {
        when(propertyService.searchProperties("Hotel", 1, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Hotel", 1, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_AllFilters_VillaType2Active() {
        when(propertyService.searchProperties("Villa", 2, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Villa", 2, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_AllFilters_VillaType2Inactive() {
        when(propertyService.searchProperties("Villa", 2, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Villa", 2, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_AllFilters_ApartmentType3Active() {
        when(propertyService.searchProperties("Apartment", 3, 1)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Apartment", 3, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_AllFilters_ApartmentType3Inactive() {
        when(propertyService.searchProperties("Apartment", 3, 0)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Apartment", 3, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test getPropertyById with various IDs
    @Test
    void testGetPropertyById_HotelId() {
        property.setPropertyID("HOT-abc123-001");
        when(propertyService.getPropertyById("HOT-abc123-001")).thenReturn(Optional.of(property));
        ResponseEntity response = propertyRestController.getPropertyById("HOT-abc123-001");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertyById_VillaId() {
        property.setPropertyID("VIL-def456-002");
        when(propertyService.getPropertyById("VIL-def456-002")).thenReturn(Optional.of(property));
        ResponseEntity response = propertyRestController.getPropertyById("VIL-def456-002");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertyById_ApartmentId() {
        property.setPropertyID("APT-ghi789-003");
        when(propertyService.getPropertyById("APT-ghi789-003")).thenReturn(Optional.of(property));
        ResponseEntity response = propertyRestController.getPropertyById("APT-ghi789-003");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertyById_NotFound1() {
        when(propertyService.getPropertyById("INVALID-1")).thenReturn(Optional.empty());
        ResponseEntity response = propertyRestController.getPropertyById("INVALID-1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertyById_NotFound2() {
        when(propertyService.getPropertyById("INVALID-2")).thenReturn(Optional.empty());
        ResponseEntity response = propertyRestController.getPropertyById("INVALID-2");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertyById_NotFound3() {
        when(propertyService.getPropertyById("INVALID-3")).thenReturn(Optional.empty());
        ResponseEntity response = propertyRestController.getPropertyById("INVALID-3");
        assertEquals(404, response.getStatusCodeValue());
    }

    // Test multiple properties
    @Test
    void testGetAllProperties_TenProperties() {
        List<Property> properties = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            properties.add(new Property());
        }
        when(propertyService.searchProperties(null, null, null)).thenReturn(properties);
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_FiftyProperties() {
        List<Property> properties = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            properties.add(new Property());
        }
        when(propertyService.searchProperties(null, null, null)).thenReturn(properties);
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_HundredProperties() {
        List<Property> properties = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            properties.add(new Property());
        }
        when(propertyService.searchProperties(null, null, null)).thenReturn(properties);
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test empty results for all combinations
    @Test
    void testGetAllProperties_EmptyResult_Type1() {
        when(propertyService.searchProperties(null, 1, null)).thenReturn(new ArrayList<>());
        ResponseEntity response = propertyRestController.getAllProperties(null, 1, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_EmptyResult_Type2() {
        when(propertyService.searchProperties(null, 2, null)).thenReturn(new ArrayList<>());
        ResponseEntity response = propertyRestController.getAllProperties(null, 2, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_EmptyResult_Type3() {
        when(propertyService.searchProperties(null, 3, null)).thenReturn(new ArrayList<>());
        ResponseEntity response = propertyRestController.getAllProperties(null, 3, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_EmptyResult_StatusActive() {
        when(propertyService.searchProperties(null, null, 1)).thenReturn(new ArrayList<>());
        ResponseEntity response = propertyRestController.getAllProperties(null, null, 1);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_EmptyResult_StatusInactive() {
        when(propertyService.searchProperties(null, null, 0)).thenReturn(new ArrayList<>());
        ResponseEntity response = propertyRestController.getAllProperties(null, null, 0);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Test different name patterns
    @Test
    void testGetAllProperties_ShortName() {
        when(propertyService.searchProperties("AB", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("AB", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_LongName() {
        when(propertyService.searchProperties("Very Long Property Name With Many Words", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Very Long Property Name With Many Words", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameWithNumbers() {
        when(propertyService.searchProperties("Hotel 123", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Hotel 123", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_NameWithSpecialChars() {
        when(propertyService.searchProperties("Hotel & Resort", null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties("Hotel & Resort", null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    // Additional edge cases
    @Test
    void testGetAllProperties_NoFilters_WithResults() {
        when(propertyService.searchProperties(null, null, null)).thenReturn(Arrays.asList(property, property, property));
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService).searchProperties(null, null, null);
    }

    @Test
    void testGetAllProperties_NoFilters_EmptyResults() {
        when(propertyService.searchProperties(null, null, null)).thenReturn(new ArrayList<>());
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllProperties_SingleProperty() {
        when(propertyService.searchProperties(null, null, null)).thenReturn(Arrays.asList(property));
        ResponseEntity response = propertyRestController.getAllProperties(null, null, null);
        assertEquals(200, response.getStatusCodeValue());
    }
}
