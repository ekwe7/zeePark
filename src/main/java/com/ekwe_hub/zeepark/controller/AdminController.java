package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.ParkingZoneRequest;
import com.ekwe_hub.zeepark.dto.request.SpotCategoryRequest;
import com.ekwe_hub.zeepark.dto.response.ParkingSpotResponse;
import com.ekwe_hub.zeepark.dto.response.ParkingZoneResponse;
import com.ekwe_hub.zeepark.dto.response.RevenueReportResponse;
import com.ekwe_hub.zeepark.dto.response.SpotCategoryResponse;
import com.ekwe_hub.zeepark.mapper.ParkingZoneMapper;
import com.ekwe_hub.zeepark.mapper.SpotCategoryMapper;
import com.ekwe_hub.zeepark.model.parking.ParkingSpot;
import com.ekwe_hub.zeepark.model.parking.ParkingZone;
import com.ekwe_hub.zeepark.model.parking.SpotCategory;
import com.ekwe_hub.zeepark.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/zones")
    public ParkingZoneResponse createZone(@Valid @RequestBody ParkingZoneRequest request) {
        ParkingZone zone = adminService.createZone(request);
        return ParkingZoneMapper.toDto(zone);
    }

    @GetMapping("/zones")
    public List<ParkingZoneResponse> getAllZones() {
        return adminService.findAllZones().stream()
                .map(ParkingZoneMapper::toDto)
                .toList();
    }

    @DeleteMapping("/zones/{zoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteZone(@PathVariable String zoneId) {
        adminService.deleteZone(zoneId);
    }

    @PostMapping("/spot-categories")
    public SpotCategoryResponse createSpotCategory(@Valid @RequestBody SpotCategoryRequest request) {
        SpotCategory category = adminService.createSpotCategory(request);
        return SpotCategoryMapper.toDto(category);
    }

    @PostMapping("/spots")
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingSpotResponse createSpot(@RequestBody Map<String, String> body) {
        ParkingSpot spot = adminService.createSpot(body.get("zoneId"), body.get("categoryId"));
        return new ParkingSpotResponse(spot.getId(), spot.getZoneId(), spot.isAvailable());
    }

    @GetMapping("/spots")
    public List<ParkingSpotResponse> getAllSpots() {
        return adminService.findAllSpots().stream()
                .map(s -> new ParkingSpotResponse(s.getId(), s.getZoneId(), s.isAvailable()))
                .toList();
    }

    @GetMapping("/spot-categories")
    public List<SpotCategoryResponse> getAllSpotCategories() {
        return adminService.findAllSpotCategories().stream()
                .map(SpotCategoryMapper::toDto)
                .toList();
    }

    @GetMapping("/reports/revenue")
    public RevenueReportResponse getRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return adminService.generateRevenueReport(from, to);
    }

    @PostMapping("/spots/{spotId}/free")
    public void forceFreeSpot(@PathVariable String spotId) {
        adminService.forceFreeSpot(spotId);
    }
}