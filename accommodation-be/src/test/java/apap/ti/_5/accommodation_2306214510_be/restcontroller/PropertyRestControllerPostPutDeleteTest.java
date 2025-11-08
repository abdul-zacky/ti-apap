package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.request.PropertyRequestDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.RoomTypeRequestDTO;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyRestControllerPostPutDeleteTest {

    @Mock
    private PropertyService propertyService;

    @Mock
    private RoomTypeService roomTypeService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private PropertyRestController propertyRestController;

    private PropertyRequestDTO propertyRequestDTO;
    private RoomTypeRequestDTO roomTypeRequestDTO;
    private Property property;
    private RoomType roomType;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyID("HOT-abc-001");
        property.setPropertyName("Test Hotel");
        property.setType(1);
        property.setProvince("DKI Jakarta");
        property.setAddress("Test Address");
        property.setDescription("Test Description");
        property.setOwnerID(UUID.randomUUID());
        property.setOwnerName("Test Owner");
        property.setTotalRoom(0);
        property.setActiveStatus(1);
        property.setIncome(0);

        roomType = new RoomType();
        roomType.setRoomTypeID("RT-HOT-001-1-001");
        roomType.setName("Standard");
        roomType.setPrice(250000);
        roomType.setCapacity(2);
        roomType.setFloor(1);
        roomType.setProperty(property);

        propertyRequestDTO = new PropertyRequestDTO();
        propertyRequestDTO.setPropertyName("Test Hotel");
        propertyRequestDTO.setType(1);
        propertyRequestDTO.setProvince("DKI Jakarta");
        propertyRequestDTO.setAddress("Test Address");
        propertyRequestDTO.setDescription("Test Description");
        propertyRequestDTO.setOwnerID(UUID.randomUUID());
        propertyRequestDTO.setOwnerName("Test Owner");

        roomTypeRequestDTO = new RoomTypeRequestDTO();
        roomTypeRequestDTO.setName("Standard");
        roomTypeRequestDTO.setPrice(250000);
        roomTypeRequestDTO.setCapacity(2);
        roomTypeRequestDTO.setFloor(1);
        roomTypeRequestDTO.setUnit(5);
        roomTypeRequestDTO.setDescription("Standard Room");
        roomTypeRequestDTO.setFacility("WiFi, TV");
    }

    // POST /create tests
    @Test
    void testCreateProperty_Success_NoRoomTypes() {
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-abc-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_Success_WithRoomTypes() {
        propertyRequestDTO.setRoomTypes(Arrays.asList(roomTypeRequestDTO));
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-abc-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(new Room());
        when(propertyService.getPropertyById(any())).thenReturn(Optional.of(property));
        when(propertyService.calculateTotalRooms(any())).thenReturn(5);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_DuplicateRoomTypeInRequest() {
        RoomTypeRequestDTO duplicate = new RoomTypeRequestDTO();
        duplicate.setName("Standard");
        duplicate.setFloor(1);
        duplicate.setUnit(3);
        propertyRequestDTO.setRoomTypes(Arrays.asList(roomTypeRequestDTO, duplicate));
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-abc-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_DuplicateRoomTypeInDatabase() {
        propertyRequestDTO.setRoomTypes(Arrays.asList(roomTypeRequestDTO));
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-abc-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(true);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_Exception() {
        when(propertyService.countPropertiesByOwner(any())).thenThrow(new RuntimeException("Database error"));
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_Hotel() {
        propertyRequestDTO.setType(1);
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_Villa() {
        propertyRequestDTO.setType(2);
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("VIL-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_Apartment() {
        propertyRequestDTO.setType(3);
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("APT-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_WithMultipleRoomTypes() {
        RoomTypeRequestDTO rt2 = new RoomTypeRequestDTO();
        rt2.setName("Deluxe");
        rt2.setFloor(2);
        rt2.setUnit(3);
        rt2.setPrice(500000);
        rt2.setCapacity(4);
        
        propertyRequestDTO.setRoomTypes(Arrays.asList(roomTypeRequestDTO, rt2));
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(new Room());
        when(propertyService.getPropertyById(any())).thenReturn(Optional.of(property));
        when(propertyService.calculateTotalRooms(any())).thenReturn(8);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    // PUT /update tests
    @Test
    void testUpdateProperty_Success() {
        Property updateRequest = new Property();
        updateRequest.setPropertyID("HOT-abc-001");
        updateRequest.setPropertyName("Updated Hotel");
        updateRequest.setAddress("Updated Address");
        updateRequest.setDescription("Updated Description");
        
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.updateProperty(updateRequest);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProperty_NotFound() {
        Property updateRequest = new Property();
        updateRequest.setPropertyID("INVALID");
        
        when(propertyService.getPropertyById("INVALID")).thenReturn(Optional.empty());
        
        ResponseEntity response = propertyRestController.updateProperty(updateRequest);
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProperty_WithRoomTypes() {
        Property updateRequest = new Property();
        updateRequest.setPropertyID("HOT-abc-001");
        updateRequest.setPropertyName("Updated Hotel");
        updateRequest.setListRoomType(Arrays.asList(roomType));
        
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(roomTypeService.getRoomTypeById(any())).thenReturn(Optional.of(roomType));
        when(roomTypeService.updateRoomType(any())).thenReturn(roomType);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.updateProperty(updateRequest);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProperty_RoomTypeNotFound() {
        Property updateRequest = new Property();
        updateRequest.setPropertyID("HOT-abc-001");
        updateRequest.setListRoomType(Arrays.asList(roomType));
        
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(roomTypeService.getRoomTypeById(any())).thenReturn(Optional.empty());
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.updateProperty(updateRequest);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    // DELETE /delete tests
    @Test
    void testDeleteProperty_Success() {
        when(propertyService.canDeleteProperty("HOT-001")).thenReturn(true);
        when(propertyService.deleteProperty("HOT-001")).thenReturn(property);
        
        ResponseEntity response = propertyRestController.deleteProperty("HOT-001");
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProperty_HasFutureBookings() {
        when(propertyService.canDeleteProperty("HOT-001")).thenReturn(false);
        
        ResponseEntity response = propertyRestController.deleteProperty("HOT-001");
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProperty_NotFound() {
        when(propertyService.canDeleteProperty("INVALID")).thenReturn(true);
        when(propertyService.deleteProperty("INVALID")).thenReturn(null);
        
        ResponseEntity response = propertyRestController.deleteProperty("INVALID");
        
        assertEquals(404, response.getStatusCodeValue());
    }

    // GET /count tests
    @Test
    void testCountProperties() {
        when(propertyService.countProperties()).thenReturn(100L);
        
        ResponseEntity response = propertyRestController.countProperties();
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCountProperties_Zero() {
        when(propertyService.countProperties()).thenReturn(0L);
        
        ResponseEntity response = propertyRestController.countProperties();
        
        assertEquals(200, response.getStatusCodeValue());
    }

    // Additional edge cases
    @Test
    void testCreateProperty_WithExistingOwnerProperties() {
        when(propertyService.countPropertiesByOwner(any())).thenReturn(5L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-006");
        when(propertyService.createProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testCreateProperty_WithManyRoomUnits() {
        roomTypeRequestDTO.setUnit(50);
        propertyRequestDTO.setRoomTypes(Arrays.asList(roomTypeRequestDTO));
        
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(new Room());
        when(propertyService.getPropertyById(any())).thenReturn(Optional.of(property));
        when(propertyService.calculateTotalRooms(any())).thenReturn(50);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
        verify(roomService, times(50)).createRoom(any());
    }

    @Test
    void testCreateProperty_WithDifferentFloors() {
        RoomTypeRequestDTO floor1 = new RoomTypeRequestDTO();
        floor1.setName("Standard");
        floor1.setFloor(1);
        floor1.setUnit(5);

        RoomTypeRequestDTO floor5 = new RoomTypeRequestDTO();
        floor5.setName("Deluxe");
        floor5.setFloor(5);
        floor5.setUnit(3);

        RoomTypeRequestDTO floor10 = new RoomTypeRequestDTO();
        floor10.setName("Suite");
        floor10.setFloor(10);
        floor10.setUnit(2);

        propertyRequestDTO.setRoomTypes(Arrays.asList(floor1, floor5, floor10));
        
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(propertyService.generatePropertyId(anyInt(), any(), anyLong())).thenReturn("HOT-001");
        when(propertyService.createProperty(any())).thenReturn(property);
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(new Room());
        when(propertyService.getPropertyById(any())).thenReturn(Optional.of(property));
        when(propertyService.calculateTotalRooms(any())).thenReturn(10);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.createProperty(propertyRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testUpdateProperty_MultipleRoomTypes() {
        RoomType rt2 = new RoomType();
        rt2.setRoomTypeID("RT-2");

        Property updateRequest = new Property();
        updateRequest.setPropertyID("HOT-001");
        updateRequest.setPropertyName("Updated Hotel");
        updateRequest.setListRoomType(Arrays.asList(roomType, rt2));
        
        when(propertyService.getPropertyById(any())).thenReturn(Optional.of(property));
        when(roomTypeService.getRoomTypeById(any())).thenReturn(Optional.of(roomType));
        when(roomTypeService.updateRoomType(any())).thenReturn(roomType);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = propertyRestController.updateProperty(updateRequest);
        
        assertEquals(200, response.getStatusCodeValue());
        verify(roomTypeService, times(2)).getRoomTypeById(any());
    }
}
