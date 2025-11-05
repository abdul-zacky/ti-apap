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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomTypeServiceImplTest {

    @Mock
    private RoomTypeRepository roomTypeRepository;

    @InjectMocks
    private RoomTypeServiceImpl roomTypeService;

    private RoomType testRoomType;
    private Property testProperty;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setPropertyID("HOT-1234-001");
        testProperty.setPropertyName("Test Hotel");

        testRoomType = new RoomType();
        testRoomType.setRoomTypeID("RT-001-Deluxe-1");
        testRoomType.setName("Deluxe");
        testRoomType.setPrice(500000);
        testRoomType.setDescription("Deluxe Room");
        testRoomType.setCapacity(2);
        testRoomType.setFacility("AC, TV, WiFi");
        testRoomType.setFloor(1);
        testRoomType.setProperty(testProperty);
    }

    @Test
    void testCreateRoomType() {
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(testRoomType);

        RoomType result = roomTypeService.createRoomType(testRoomType);

        assertNotNull(result);
        assertEquals("RT-001-Deluxe-1", result.getRoomTypeID());
        assertEquals("Deluxe", result.getName());
        assertEquals(500000, result.getPrice());
        verify(roomTypeRepository, times(1)).save(testRoomType);
    }

    @Test
    void testGetRoomTypeById() {
        when(roomTypeRepository.findById("RT-001-Deluxe-1")).thenReturn(Optional.of(testRoomType));

        Optional<RoomType> result = roomTypeService.getRoomTypeById("RT-001-Deluxe-1");

        assertTrue(result.isPresent());
        assertEquals("Deluxe", result.get().getName());
        assertEquals(500000, result.get().getPrice());
        verify(roomTypeRepository, times(1)).findById("RT-001-Deluxe-1");
    }

    @Test
    void testGetRoomTypeById_NotFound() {
        when(roomTypeRepository.findById("INVALID")).thenReturn(Optional.empty());

        Optional<RoomType> result = roomTypeService.getRoomTypeById("INVALID");

        assertFalse(result.isPresent());
        verify(roomTypeRepository, times(1)).findById("INVALID");
    }

    @Test
    void testUpdateRoomType() {
        when(roomTypeRepository.save(any(RoomType.class))).thenReturn(testRoomType);

        testRoomType.setPrice(600000);
        RoomType result = roomTypeService.updateRoomType(testRoomType);

        assertNotNull(result);
        assertEquals(600000, result.getPrice());
        verify(roomTypeRepository, times(1)).save(testRoomType);
    }

    @Test
    void testGenerateRoomTypeId() {
        String result = roomTypeService.generateRoomTypeId(1, "Deluxe", 2);

        assertNotNull(result);
        assertTrue(result.contains("002"));
        assertTrue(result.contains("Deluxe"));
        assertTrue(result.contains("2"));
    }

    @Test
    void testGenerateRoomTypeId_WithSpecialCharacters() {
        String result = roomTypeService.generateRoomTypeId(0, "Suite Room", 1);

        assertNotNull(result);
        assertTrue(result.contains("001"));
        assertTrue(result.contains("Suite Room"));
        assertTrue(result.contains("1"));
    }

    @Test
    void testIsDuplicateRoomType_Duplicate() {
        when(roomTypeRepository.existsByPropertyAndNameAndFloor(testProperty, "Deluxe", 1))
                .thenReturn(true);

        boolean result = roomTypeService.isDuplicateRoomType(testProperty, "Deluxe", 1);

        assertTrue(result);
        verify(roomTypeRepository, times(1))
                .existsByPropertyAndNameAndFloor(testProperty, "Deluxe", 1);
    }

    @Test
    void testIsDuplicateRoomType_NotDuplicate() {
        when(roomTypeRepository.existsByPropertyAndNameAndFloor(testProperty, "Suite", 2))
                .thenReturn(false);

        boolean result = roomTypeService.isDuplicateRoomType(testProperty, "Suite", 2);

        assertFalse(result);
        verify(roomTypeRepository, times(1))
                .existsByPropertyAndNameAndFloor(testProperty, "Suite", 2);
    }

    @Test
    void testFindByPropertyAndNameAndFloor() {
        when(roomTypeRepository.findByPropertyAndNameAndFloor(testProperty, "Deluxe", 1))
                .thenReturn(Optional.of(testRoomType));

        Optional<RoomType> result = roomTypeRepository.findByPropertyAndNameAndFloor(testProperty, "Deluxe", 1);

        assertTrue(result.isPresent());
        assertEquals("Deluxe", result.get().getName());
        assertEquals(1, result.get().getFloor());
        verify(roomTypeRepository, times(1))
                .findByPropertyAndNameAndFloor(testProperty, "Deluxe", 1);
    }
}
