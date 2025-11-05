package apap.ti._5.accommodation_2306214510_be.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @Column(name = "room_id", nullable = false, unique = true)
    private String roomID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "availability_status", nullable = false)
    private int availabilityStatus; // 0: Unavailable, 1: Available

    @Column(name = "active_room", nullable = false)
    private int activeRoom; // 0: NonActive, 1: Active

    @Column(name = "maintenance_start")
    private LocalDateTime maintenanceStart;

    @Column(name = "maintenance_end")
    private LocalDateTime maintenanceEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id", referencedColumnName = "room_type_id", nullable = false)
    @JsonBackReference
    private RoomType roomType;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        if (availabilityStatus == 0) {
            availabilityStatus = 1; // Default to available
        }
        if (activeRoom == 0) {
            activeRoom = 1; // Default to active
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
