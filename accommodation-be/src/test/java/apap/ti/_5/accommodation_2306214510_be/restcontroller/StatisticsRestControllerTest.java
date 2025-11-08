package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.model.AccommodationBooking;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.service.AccommodationBookingService;
import apap.ti._5.accommodation_2306214510_be.service.PropertyService;
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
class StatisticsRestControllerTest {

    @Mock
    private PropertyService propertyService;

    @Mock
    private AccommodationBookingService bookingService;

    @InjectMocks
    private StatisticsRestController statisticsRestController;

    @Test
    void testGetPropertiesByType_EmptyList() {
        when(propertyService.getAllProperties()).thenReturn(new ArrayList<>());
        
        ResponseEntity response = statisticsRestController.getPropertiesByType();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService).getAllProperties();
    }

    @Test
    void testGetPropertiesByType_OnlyHotels() {
        Property hotel1 = new Property();
        hotel1.setType(1);
        Property hotel2 = new Property();
        hotel2.setType(1);
        
        when(propertyService.getAllProperties()).thenReturn(Arrays.asList(hotel1, hotel2));
        
        ResponseEntity response = statisticsRestController.getPropertiesByType();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        verify(propertyService).getAllProperties();
    }

    @Test
    void testGetPropertiesByType_MixedTypes() {
        Property hotel = new Property();
        hotel.setType(1);
        Property villa = new Property();
        villa.setType(2);
        Property apartment = new Property();
        apartment.setType(3);
        
        when(propertyService.getAllProperties()).thenReturn(Arrays.asList(hotel, villa, apartment));
        
        ResponseEntity response = statisticsRestController.getPropertiesByType();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertiesByType_OnlyVillas() {
        Property villa1 = new Property();
        villa1.setType(2);
        Property villa2 = new Property();
        villa2.setType(2);
        Property villa3 = new Property();
        villa3.setType(2);
        
        when(propertyService.getAllProperties()).thenReturn(Arrays.asList(villa1, villa2, villa3));
        
        ResponseEntity response = statisticsRestController.getPropertiesByType();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertiesByType_OnlyApartments() {
        Property apt1 = new Property();
        apt1.setType(3);
        Property apt2 = new Property();
        apt2.setType(3);
        
        when(propertyService.getAllProperties()).thenReturn(Arrays.asList(apt1, apt2));
        
        ResponseEntity response = statisticsRestController.getPropertiesByType();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingsByStatus_Empty() {
        when(bookingService.getBookingsByStatus(anyInt())).thenReturn(new ArrayList<>());
        
        ResponseEntity response = statisticsRestController.getBookingsByStatus();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetBookingsByStatus_WithData() {
        AccommodationBooking booking = new AccommodationBooking();
        when(bookingService.getBookingsByStatus(0)).thenReturn(Arrays.asList(booking, booking));
        when(bookingService.getBookingsByStatus(1)).thenReturn(Arrays.asList(booking));
        when(bookingService.getBookingsByStatus(2)).thenReturn(new ArrayList<>());
        when(bookingService.getBookingsByStatus(3)).thenReturn(Arrays.asList(booking));
        when(bookingService.getBookingsByStatus(4)).thenReturn(Arrays.asList(booking, booking, booking));
        
        ResponseEntity response = statisticsRestController.getBookingsByStatus();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetTopPropertiesByIncome_Empty() {
        when(propertyService.getAllProperties()).thenReturn(new ArrayList<>());
        
        ResponseEntity response = statisticsRestController.getTopPropertiesByIncome();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetTopPropertiesByIncome_LessThanFive() {
        Property p1 = new Property();
        p1.setPropertyName("Property 1");
        p1.setIncome(1000);
        
        Property p2 = new Property();
        p2.setPropertyName("Property 2");
        p2.setIncome(2000);
        
        when(propertyService.getAllProperties()).thenReturn(Arrays.asList(p1, p2));
        
        ResponseEntity response = statisticsRestController.getTopPropertiesByIncome();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetTopPropertiesByIncome_MoreThanFive() {
        List<Property> properties = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Property p = new Property();
            p.setPropertyName("Property " + i);
            p.setIncome(i * 1000);
            properties.add(p);
        }
        
        when(propertyService.getAllProperties()).thenReturn(properties);
        
        ResponseEntity response = statisticsRestController.getTopPropertiesByIncome();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertiesByProvince_Empty() {
        when(propertyService.getAllProperties()).thenReturn(new ArrayList<>());
        
        ResponseEntity response = statisticsRestController.getPropertiesByProvince();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertiesByProvince_SingleProvince() {
        Property p1 = new Property();
        p1.setProvince("DKI Jakarta");
        Property p2 = new Property();
        p2.setProvince("DKI Jakarta");
        
        when(propertyService.getAllProperties()).thenReturn(Arrays.asList(p1, p2));
        
        ResponseEntity response = statisticsRestController.getPropertiesByProvince();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPropertiesByProvince_MultipleProvinces() {
        Property p1 = new Property();
        p1.setProvince("DKI Jakarta");
        Property p2 = new Property();
        p2.setProvince("Jawa Barat");
        Property p3 = new Property();
        p3.setProvince("DKI Jakarta");
        
        when(propertyService.getAllProperties()).thenReturn(Arrays.asList(p1, p2, p3));
        
        ResponseEntity response = statisticsRestController.getPropertiesByProvince();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetStatisticsSummary() {
        Property p1 = new Property();
        p1.setActiveStatus(1);
        p1.setIncome(1000);
        
        Property p2 = new Property();
        p2.setActiveStatus(0);
        p2.setIncome(2000);
        
        AccommodationBooking booking = new AccommodationBooking();
        
        when(propertyService.countProperties()).thenReturn(2L);
        when(bookingService.countAllBookings()).thenReturn(5L);
        when(propertyService.getAllProperties()).thenReturn(Arrays.asList(p1, p2));
        when(bookingService.getBookingsByStatus(1)).thenReturn(Arrays.asList(booking, booking));
        
        ResponseEntity response = statisticsRestController.getStatisticsSummary();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetStatisticsSummary_Empty() {
        when(propertyService.countProperties()).thenReturn(0L);
        when(bookingService.countAllBookings()).thenReturn(0L);
        when(propertyService.getAllProperties()).thenReturn(new ArrayList<>());
        when(bookingService.getBookingsByStatus(1)).thenReturn(new ArrayList<>());
        
        ResponseEntity response = statisticsRestController.getStatisticsSummary();
        
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
