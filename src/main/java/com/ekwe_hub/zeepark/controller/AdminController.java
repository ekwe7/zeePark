package com.ekwe_hub.zeepark.controller;

import com.ekwe_hub.zeepark.dto.request.ParkingZoneRequest;
import com.ekwe_hub.zeepark.dto.request.SpotCategoryRequest;
import com.ekwe_hub.zeepark.dto.response.ParkingZoneResponse;
import com.ekwe_hub.zeepark.dto.response.RevenueReportResponse;
import com.ekwe_hub.zeepark.dto.response.SpotCategoryResponse;
import com.ekwe_hub.zeepark.mapper.ParkingZoneMapper;
import com.ekwe_hub.zeepark.mapper.SpotCategoryMapper;
import com.ekwe_hub.zeepark.model.parking.ParkingZone;
import com.ekwe_hub.zeepark.model.parking.SpotCategory;
import com.ekwe_hub.zeepark.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @PostMapping("/spot-categories")
    public SpotCategoryResponse createSpotCategory(@Valid @RequestBody SpotCategoryRequest request) {
        SpotCategory category = adminService.createSpotCategory(request);
        return SpotCategoryMapper.toDto(category);
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