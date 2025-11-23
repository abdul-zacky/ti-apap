package apap.ti._5.accommodation_2306214510_be.restcontroller;

import apap.ti._5.accommodation_2306214510_be.dto.request.MaintenanceRequestDTO;
import apap.ti._5.accommodation_2306214510_be.dto.request.RoomTypeRequestDTO;
import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.Room;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.repository.RoomRepository;
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

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomRestControllerPostPutTest {

    @Mock
    private PropertyService propertyService;

    @Mock
    private RoomTypeService roomTypeService;

    @Mock
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomRestController roomRestController;

    private Property property;
    private RoomType roomType;
    private Room room;
    private RoomTypeRequestDTO roomTypeRequestDTO;
    private MaintenanceRequestDTO maintenanceRequestDTO;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyID("HOT-abc-001");
        property.setOwnerID(UUID.randomUUID());
        property.setTotalRoom(0);

        roomType = new RoomType();
        roomType.setRoomTypeID("RT-HOT-001-1-001");
        roomType.setName("Standard");
        roomType.setPrice(250000);
        roomType.setFloor(1);
        roomType.setProperty(property);

        room = new Room();
        room.setRoomID("HOT-abc-001-101");
        room.setName("101");
        room.setRoomType(roomType);

        roomTypeRequestDTO = new RoomTypeRequestDTO();
        roomTypeRequestDTO.setName("Standard");
        roomTypeRequestDTO.setPrice(250000);
        roomTypeRequestDTO.setCapacity(2);
        roomTypeRequestDTO.setFloor(1);
        roomTypeRequestDTO.setUnit(5);
        roomTypeRequestDTO.setDescription("Standard Room");
        roomTypeRequestDTO.setFacility("WiFi, TV");

        maintenanceRequestDTO = new MaintenanceRequestDTO();
        maintenanceRequestDTO.setRoomID("HOT-abc-001-101");
        maintenanceRequestDTO.setMaintenanceStart(LocalDateTime.now().plusDays(1));
        maintenanceRequestDTO.setMaintenanceEnd(LocalDateTime.now().plusDays(3));
    }

    // POST /updateroom tests (add room type)
    @Test
    void testAddRoomType_Success() {
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(room);
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(propertyService.calculateTotalRooms(any())).thenReturn(5);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testAddRoomType_PropertyNotFound() {
        when(propertyService.getPropertyById("INVALID")).thenReturn(Optional.empty());
        
        ResponseEntity response = roomRestController.addRoomType("INVALID", roomTypeRequestDTO);
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testAddRoomType_DuplicateRoomType() {
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(true);
        
        ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testAddRoomType_Exception() {
        when(propertyService.getPropertyById("HOT-abc-001")).thenThrow(new RuntimeException("Database error"));
        
        ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testAddRoomType_WithMultipleUnits() {
        roomTypeRequestDTO.setUnit(10);
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(room);
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(propertyService.calculateTotalRooms(any())).thenReturn(10);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
        verify(roomService, times(10)).createRoom(any());
    }

    @Test
    void testAddRoomType_DifferentFloors() {
        // Floor 1
        roomTypeRequestDTO.setFloor(1);
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(room);
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(propertyService.calculateTotalRooms(any())).thenReturn(5);
        when(propertyService.updateProperty(any())).thenReturn(property);
        ResponseEntity response1 = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        assertEquals(201, response1.getStatusCodeValue());

        // Floor 5
        roomTypeRequestDTO.setFloor(5);
        ResponseEntity response2 = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        assertEquals(201, response2.getStatusCodeValue());

        // Floor 10
        roomTypeRequestDTO.setFloor(10);
        ResponseEntity response3 = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        assertEquals(201, response3.getStatusCodeValue());
    }

    @Test
    void testAddRoomType_PropertyNotFoundAfterUpdate() {
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(room);
        when(propertyService.getPropertyById("HOT-abc-001"))
                .thenReturn(Optional.of(property))
                .thenReturn(Optional.empty());
        
        ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        
        assertEquals(500, response.getStatusCodeValue());
    }

    // POST /maintenance/add tests
    @Test
    void testScheduleRoomMaintenance_Success() {
        when(roomService.getRoomById("HOT-abc-001-101")).thenReturn(Optional.of(room));
        when(roomService.updateRoom(any())).thenReturn(room);
        
        ResponseEntity response = roomRestController.scheduleRoomMaintenance(maintenanceRequestDTO);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testScheduleRoomMaintenance_RoomNotFound() {
        when(roomService.getRoomById("INVALID")).thenReturn(Optional.empty());
        
        ResponseEntity response = roomRestController.scheduleRoomMaintenance(maintenanceRequestDTO);
        
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testScheduleRoomMaintenance_Exception() {
        when(roomService.getRoomById(any())).thenThrow(new RuntimeException("Database error"));
        
        ResponseEntity response = roomRestController.scheduleRoomMaintenance(maintenanceRequestDTO);
        
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    void testScheduleRoomMaintenance_DifferentDateRanges() {
        // Short maintenance (1 day)
        maintenanceRequestDTO.setMaintenanceStart(LocalDateTime.now().plusDays(1));
        maintenanceRequestDTO.setMaintenanceEnd(LocalDateTime.now().plusDays(2));
        when(roomService.getRoomById(any())).thenReturn(Optional.of(room));
        when(roomService.updateRoom(any())).thenReturn(room);
        ResponseEntity response1 = roomRestController.scheduleRoomMaintenance(maintenanceRequestDTO);
        assertEquals(200, response1.getStatusCodeValue());

        // Long maintenance (30 days)
        maintenanceRequestDTO.setMaintenanceStart(LocalDateTime.now().plusDays(1));
        maintenanceRequestDTO.setMaintenanceEnd(LocalDateTime.now().plusDays(31));
        ResponseEntity response2 = roomRestController.scheduleRoomMaintenance(maintenanceRequestDTO);
        assertEquals(200, response2.getStatusCodeValue());
    }

    // Additional edge cases
    @Test
    void testAddRoomType_WithExistingRoomsOnFloor() {
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
        when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
        when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
        when(roomTypeService.createRoomType(any())).thenReturn(roomType);
        when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(10L);
        when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
        when(roomService.createRoom(any())).thenReturn(room);
        when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
        when(propertyService.calculateTotalRooms(any())).thenReturn(15);
        when(propertyService.updateProperty(any())).thenReturn(property);
        
        ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
        
        assertEquals(201, response.getStatusCodeValue());
    }

    @Test
    void testAddRoomType_DifferentRoomTypeNames() {
        String[] roomTypeNames = {"Standard", "Deluxe", "Suite", "Presidential", "Executive"};
        
        for (String name : roomTypeNames) {
            roomTypeRequestDTO.setName(name);
            when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
            when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
            when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
            when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
            when(roomTypeService.createRoomType(any())).thenReturn(roomType);
            when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
            when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
            when(roomService.createRoom(any())).thenReturn(room);
            when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
            when(propertyService.calculateTotalRooms(any())).thenReturn(5);
            when(propertyService.updateProperty(any())).thenReturn(property);
            
            ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
            assertEquals(201, response.getStatusCodeValue());
        }
    }

    @Test
    void testAddRoomType_DifferentPrices() {
        int[] prices = {100000, 250000, 500000, 1000000, 2000000};
        
        for (int price : prices) {
            roomTypeRequestDTO.setPrice(price);
            when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
            when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
            when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
            when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
            when(roomTypeService.createRoomType(any())).thenReturn(roomType);
            when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
            when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
            when(roomService.createRoom(any())).thenReturn(room);
            when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
            when(propertyService.calculateTotalRooms(any())).thenReturn(5);
            when(propertyService.updateProperty(any())).thenReturn(property);
            
            ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
            assertEquals(201, response.getStatusCodeValue());
        }
    }

    @Test
    void testAddRoomType_DifferentCapacities() {
        int[] capacities = {1, 2, 4, 6, 8};
        
        for (int capacity : capacities) {
            roomTypeRequestDTO.setCapacity(capacity);
            when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
            when(roomTypeService.isDuplicateRoomType(any(), any(), anyInt())).thenReturn(false);
            when(propertyService.countPropertiesByOwner(any())).thenReturn(0L);
            when(roomTypeService.generateRoomTypeId(anyLong(), any(), anyInt())).thenReturn("RT-1");
            when(roomTypeService.createRoomType(any())).thenReturn(roomType);
            when(roomService.countRoomsByPropertyAndFloor(any(), anyInt())).thenReturn(0L);
            when(roomService.generateRoomId(any(), anyInt())).thenReturn("ROOM-1");
            when(roomService.createRoom(any())).thenReturn(room);
            when(propertyService.getPropertyById("HOT-abc-001")).thenReturn(Optional.of(property));
            when(propertyService.calculateTotalRooms(any())).thenReturn(5);
            when(propertyService.updateProperty(any())).thenReturn(property);
            
            ResponseEntity response = roomRestController.addRoomType("HOT-abc-001", roomTypeRequestDTO);
            assertEquals(201, response.getStatusCodeValue());
        }
    }

    @Test
    void testScheduleRoomMaintenance_FutureDate() {
        maintenanceRequestDTO.setMaintenanceStart(LocalDateTime.now().plusMonths(6));
        maintenanceRequestDTO.setMaintenanceEnd(LocalDateTime.now().plusMonths(6).plusDays(7));
        when(roomService.getRoomById(any())).thenReturn(Optional.of(room));
        when(roomService.updateRoom(any())).thenReturn(room);
        
        ResponseEntity response = roomRestController.scheduleRoomMaintenance(maintenanceRequestDTO);
        
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testScheduleRoomMaintenance_MultipleRooms() {
        String[] roomIds = {"HOT-001-101", "HOT-001-102", "HOT-001-201", "VIL-002-301"};
        
        for (String roomId : roomIds) {
            maintenanceRequestDTO.setRoomID(roomId);
            when(roomService.getRoomById(roomId)).thenReturn(Optional.of(room));
            when(roomService.updateRoom(any())).thenReturn(room);
            
            ResponseEntity response = roomRestController.scheduleRoomMaintenance(maintenanceRequestDTO);
            assertEquals(200, response.getStatusCodeValue());
        }
    }
}
