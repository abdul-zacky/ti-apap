package apap.ti._5.accommodation_2306214510_be.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "property")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {

    @Id
    @Column(name = "property_id", nullable = false, unique = true)
    private String propertyID;

    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(name = "type", nullable = false)
    private int type; // 1: Hotel, 2: Villa, 3: Apartment

    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(name = "province", nullable = false)
    private int province;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "total_room", nullable = false)
    private int totalRoom;

    @Column(name = "active_status", nullable = false)
    private int activeStatus; // 0: NonActive, 1: Active

    @Column(name = "income", nullable = false)
    private int income;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<RoomType> listRoomType = new ArrayList<>();

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerID;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        if (activeStatus == 0) {
            activeStatus = 1; // Default to active
        }
        if (income == 0) {
            income = 0; // Default income
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
