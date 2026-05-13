package com.ekwe_hub.zeepark.model.parking;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "Parking_Session")
public class ParkingSession {
    @Id
    private String userId; // Primary key - matches MongoDB _id

    private String vehicleId;
    private String spotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private SessionStatus status;
    private int duration;

    // Audit fields (previously inherited from BaseDocument)
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Version
    private Long version;
}
