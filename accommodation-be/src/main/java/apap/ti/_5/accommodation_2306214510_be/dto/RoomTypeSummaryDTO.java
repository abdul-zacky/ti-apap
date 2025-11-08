package apap.ti._5.accommodation_2306214510_be.dto;

public class RoomTypeSummaryDTO {
    private String roomTypeID;
    private String name;
    private int capacity;
    private int price;
    private int floor;
    private String description;
    private String facility;
    private long totalRooms;
    private long availableRooms;

    public RoomTypeSummaryDTO() {
    }

    public RoomTypeSummaryDTO(String roomTypeID, String name, int capacity, int price, 
                              int floor, String description, String facility, 
                              long totalRooms, long availableRooms) {
        this.roomTypeID = roomTypeID;
        this.name = name;
        this.capacity = capacity;
        this.price = price;
        this.floor = floor;
        this.description = description;
        this.facility = facility;
        this.totalRooms = totalRooms;
        this.availableRooms = availableRooms;
    }

    // Getters and Setters
    public String getRoomTypeID() {
        return roomTypeID;
    }

    public void setRoomTypeID(String roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public long getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(long totalRooms) {
        this.totalRooms = totalRooms;
    }

    public long getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(long availableRooms) {
        this.availableRooms = availableRooms;
    }
}
