package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.request.ParkingZoneRequest;
import com.ekwe_hub.zeepark.dto.request.SpotCategoryRequest;
import com.ekwe_hub.zeepark.dto.response.RevenueReportResponse;
import com.ekwe_hub.zeepark.model.parking.ParkingSpot;
import com.ekwe_hub.zeepark.model.parking.ParkingZone;
import com.ekwe_hub.zeepark.model.parking.SpotCategory;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    ParkingZone createZone(ParkingZoneRequest request);
    void deleteZone(String zoneId);
    ParkingSpot createSpot(String zoneId, String categoryId);
    List<ParkingSpot> findAllSpots();
    List<ParkingZone> findAllZones();
    SpotCategory createSpotCategory(SpotCategoryRequest request);
    List<SpotCategory> findAllSpotCategories();
    RevenueReportResponse generateRevenueReport(LocalDate from, LocalDate to);
    void forceFreeSpot(String spotId);
}