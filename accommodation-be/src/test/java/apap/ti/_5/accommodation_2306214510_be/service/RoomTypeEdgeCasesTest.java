package apap.ti._5.accommodation_2306214510_be.service;

import apap.ti._5.accommodation_2306214510_be.model.Property;
import apap.ti._5.accommodation_2306214510_be.model.RoomType;
import apap.ti._5.accommodation_2306214510_be.repository.RoomTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomTypeEdgeCasesTest {

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @InjectMocks
    private RoomTypeServiceImpl roomTypeService;

    private RoomType roomType;
    private Property property;

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyID("HOT-abc123-001");
        
        roomType = new RoomType();
        roomType.setRoomTypeID("001–Standard–1");
        roomType.setName("Standard");
        roomType.setFloor(1);
        roomType.setProperty(property);
    }

    // generateRoomTypeId tests
    @Test
    void testGenerateRoomTypeId_SmallNumber() {
        String result = roomTypeService.generateRoomTypeId(0, "Deluxe", 1);
        assertEquals("001–Deluxe–1", result);
    }

    @Test
    void testGenerateRoomTypeId_MediumNumber() {
        String result = roomTypeService.generateRoomTypeId(49, "Suite", 2);
        assertEquals("050–Suite–2", result);
    }

    @Test
    void testGenerateRoomTypeId_LargeNumber() {
        String result = roomTypeService.generateRoomTypeId(999, "Presidential", 10);
        assertEquals("1000–Presidential–10", result);
    }

    @Test
    void testGenerateRoomTypeId_FirstFloor() {
        String result = roomTypeService.generateRoomTypeId(5, "Economy", 1);
        assertEquals("006–Economy–1", result);
    }

    @Test
    void testGenerateRoomTypeId_TopFloor() {
        String result = roomTypeService.generateRoomTypeId(10, "Penthouse", 20);
        assertEquals("011–Penthouse–20", result);
    }

    // createRoomType tests
    @Test
    void testCreateRoomType_Success() {
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);
        
        RoomType result = roomTypeService.createRoomType(roomType);
        
        assertNotNull(result);
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getUpdatedDate());
        verify(roomTypeRepository).save(roomType);
    }

    @Test
    void testCreateRoomType_WithAllFields() {
        roomType.setName("Deluxe Suite");
        roomType.setFloor(5);
        roomType.setCapacity(4);
        roomType.setPrice(500000);
        
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);
        
        RoomType result = roomTypeService.createRoomType(roomType);
        
        assertNotNull(result);
        assertEquals("Deluxe Suite", result.getName());
        assertEquals(5, result.getFloor());
        assertEquals(4, result.getCapacity());
        assertEquals(500000, result.getPrice());
    }

    // createMultipleRoomTypes tests
    @Test
    void testCreateMultipleRoomTypes_TwoRoomTypes() {
        RoomType roomType1 = new RoomType();
        roomType1.setName("Standard");
        roomType1.setFloor(1);
        
        RoomType roomType2 = new RoomType();
        roomType2.setName("Deluxe");
        roomType2.setFloor(2);
        
        List<RoomType> roomTypes = Arrays.asList(roomType1, roomType2);
        
        when(roomTypeRepository.save(any(RoomType.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        List<RoomType> result = roomTypeService.createMultipleRoomTypes(roomTypes, property);
        
        assertEquals(2, result.size());
        verify(roomTypeRepository, times(2)).save(any(RoomType.class));
        assertEquals(property, result.get(0).getProperty());
        assertEquals(property, result.get(1).getProperty());
    }

    @Test
    void testCreateMultipleRoomTypes_EmptyList() {
        List<RoomType> result = roomTypeService.createMultipleRoomTypes(new ArrayList<>(), property);
        
        assertTrue(result.isEmpty());
        verify(roomTypeRepository, never()).save(any(RoomType.class));
    }

    @Test
    void testCreateMultipleRoomTypes_SingleRoomType() {
        List<RoomType> roomTypes = Arrays.asList(roomType);
        
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);
        
        List<RoomType> result = roomTypeService.createMultipleRoomTypes(roomTypes, property);
        
        assertEquals(1, result.size());
        verify(roomTypeRepository, times(1)).save(any(RoomType.class));
    }

    // getAllRoomTypes tests
    @Test
    void testGetAllRoomTypes_MultipleRoomTypes() {
        List<RoomType> roomTypes = Arrays.asList(roomType, new RoomType(), new RoomType());
        when(roomTypeRepository.findAllByOrderByCreatedDateDesc()).thenReturn(roomTypes);
        
        List<RoomType> result = roomTypeService.getAllRoomTypes();
        
        assertEquals(3, result.size());
        verify(roomTypeRepository).findAllByOrderByCreatedDateDesc();
    }

    @Test
    void testGetAllRoomTypes_EmptyList() {
        when(roomTypeRepository.findAllByOrderByCreatedDateDesc()).thenReturn(new ArrayList<>());
        
        List<RoomType> result = roomTypeService.getAllRoomTypes();
        
        assertTrue(result.isEmpty());
    }

    // getRoomTypeById tests
    @Test
    void testGetRoomTypeById_Found() {
        when(roomTypeRepository.findById("001–Standard–1")).thenReturn(Optional.of(roomType));
        
        Optional<RoomType> result = roomTypeService.getRoomTypeById("001–Standard–1");
        
        assertTrue(result.isPresent());
        assertEquals("001–Standard–1", result.get().getRoomTypeID());
    }

    @Test
    void testGetRoomTypeById_NotFound() {
        when(roomTypeRepository.findById("999–NotExist–99")).thenReturn(Optional.empty());
        
        Optional<RoomType> result = roomTypeService.getRoomTypeById("999–NotExist–99");
        
        assertFalse(result.isPresent());
    }

    // getRoomTypesByProperty tests
    @Test
    void testGetRoomTypesByProperty_Found() {
        List<RoomType> roomTypes = Arrays.asList(roomType, new RoomType());
        when(roomTypeRepository.findByProperty(property)).thenReturn(roomTypes);
        
        List<RoomType> result = roomTypeService.getRoomTypesByProperty(property);
        
        assertEquals(2, result.size());
    }

    @Test
    void testGetRoomTypesByProperty_EmptyList() {
        when(roomTypeRepository.findByProperty(property)).thenReturn(new ArrayList<>());
        
        List<RoomType> result = roomTypeService.getRoomTypesByProperty(property);
        
        assertTrue(result.isEmpty());
    }

    // getRoomTypesByPropertyId tests
    @Test
    void testGetRoomTypesByPropertyId_Found() {
        List<RoomType> roomTypes = Arrays.asList(roomType);
        when(roomTypeRepository.findByPropertyPropertyID("HOT-abc123-001")).thenReturn(roomTypes);
        
        List<RoomType> result = roomTypeService.getRoomTypesByPropertyId("HOT-abc123-001");
        
        assertEquals(1, result.size());
    }

    @Test
    void testGetRoomTypesByPropertyId_NotFound() {
        when(roomTypeRepository.findByPropertyPropertyID("INVALID-ID")).thenReturn(new ArrayList<>());
        
        List<RoomType> result = roomTypeService.getRoomTypesByPropertyId("INVALID-ID");
        
        assertTrue(result.isEmpty());
    }

    // updateRoomType tests
    @Test
    void testUpdateRoomType_Success() {
        roomType.setName("Updated Standard");
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);
        
        RoomType result = roomTypeService.updateRoomType(roomType);
        
        assertNotNull(result);
        assertNotNull(result.getUpdatedDate());
        assertEquals("Updated Standard", result.getName());
        verify(roomTypeRepository).save(roomType);
    }

    @Test
    void testUpdateRoomType_ChangePricing() {
        roomType.setPrice(750000);
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);
        
        RoomType result = roomTypeService.updateRoomType(roomType);
        
        assertEquals(750000, result.getPrice());
    }

    @Test
    void testUpdateRoomType_ChangeCapacity() {
        roomType.setCapacity(6);
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(roomType);
        
        RoomType result = roomTypeService.updateRoomType(roomType);
        
        assertEquals(6, result.getCapacity());
    }

    // isDuplicateRoomType tests
    @Test
    void testIsDuplicateRoomType_Exists() {
        when(roomTypeRepository.existsByPropertyAndNameAndFloor(property, "Standard", 1))
            .thenReturn(true);
        
        boolean result = roomTypeService.isDuplicateRoomType(property, "Standard", 1);
        
        assertTrue(result);
    }

    @Test
    void testIsDuplicateRoomType_NotExists() {
        when(roomTypeRepository.existsByPropertyAndNameAndFloor(property, "Deluxe", 2))
            .thenReturn(false);
        
        boolean result = roomTypeService.isDuplicateRoomType(property, "Deluxe", 2);
        
        assertFalse(result);
    }

    @Test
    void testIsDuplicateRoomType_DifferentFloorSameName() {
        when(roomTypeRepository.existsByPropertyAndNameAndFloor(property, "Standard", 2))
            .thenReturn(false);
        
        boolean result = roomTypeService.isDuplicateRoomType(property, "Standard", 2);
        
        assertFalse(result); // Same name but different floor is not duplicate
    }

    // checkDuplicatesInList tests
    @Test
    void testCheckDuplicatesInList_NoDuplicates() {
        RoomType rt1 = new RoomType();
        rt1.setProperty(property);
        rt1.setName("Standard");
        rt1.setFloor(1);
        
        RoomType rt2 = new RoomType();
        rt2.setProperty(property);
        rt2.setName("Deluxe");
        rt2.setFloor(2);
        
        List<RoomType> roomTypes = Arrays.asList(rt1, rt2);
        
        when(roomTypeRepository.existsByPropertyAndNameAndFloor(any(), anyString(), anyInt()))
            .thenReturn(false);
        
        boolean result = roomTypeService.checkDuplicatesInList(roomTypes);
        
        assertFalse(result);
    }

    @Test
    void testCheckDuplicatesInList_WithinListDuplicate() {
        RoomType rt1 = new RoomType();
        rt1.setProperty(property);
        rt1.setName("Standard");
        rt1.setFloor(1);
        
        RoomType rt2 = new RoomType();
        rt2.setProperty(property);
        rt2.setName("Standard");
        rt2.setFloor(1);
        
        List<RoomType> roomTypes = Arrays.asList(rt1, rt2);
        
        boolean result = roomTypeService.checkDuplicatesInList(roomTypes);
        
        assertTrue(result); // Duplicate found within the list
    }

    @Test
    void testCheckDuplicatesInList_DatabaseDuplicate() {
        RoomType rt1 = new RoomType();
        rt1.setProperty(property);
        rt1.setName("Standard");
        rt1.setFloor(1);
        
        List<RoomType> roomTypes = Arrays.asList(rt1);
        
        when(roomTypeRepository.existsByPropertyAndNameAndFloor(property, "Standard", 1))
            .thenReturn(true);
        
        boolean result = roomTypeService.checkDuplicatesInList(roomTypes);
        
        assertTrue(result); // Duplicate found in database
    }

    @Test
    void testCheckDuplicatesInList_EmptyList() {
        boolean result = roomTypeService.checkDuplicatesInList(new ArrayList<>());
        
        assertFalse(result);
    }

    @Test
    void testCheckDuplicatesInList_SingleItem() {
        RoomType rt1 = new RoomType();
        rt1.setProperty(property);
        rt1.setName("Standard");
        rt1.setFloor(1);
        
        List<RoomType> roomTypes = Arrays.asList(rt1);
        
        when(roomTypeRepository.existsByPropertyAndNameAndFloor(property, "Standard", 1))
            .thenReturn(false);
        
        boolean result = roomTypeService.checkDuplicatesInList(roomTypes);
        
        assertFalse(result);
    }
}
