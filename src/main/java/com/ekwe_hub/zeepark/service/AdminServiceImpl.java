package com.ekwe_hub.zeepark.service;

import com.ekwe_hub.zeepark.dto.request.ParkingZoneRequest;
import com.ekwe_hub.zeepark.dto.request.SpotCategoryRequest;
import com.ekwe_hub.zeepark.dto.response.RevenueReportResponse;
import com.ekwe_hub.zeepark.exception.ResourceNotFoundException;
import com.ekwe_hub.zeepark.model.parking.*;
import com.ekwe_hub.zeepark.model.payment.Payment;
import com.ekwe_hub.zeepark.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ParkingZoneRepository zoneRepository;
    private final SpotCategoryRepository categoryRepository;
    private final ParkingSpotRepository spotRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public ParkingZone createZone(ParkingZoneRequest request) {
        ParkingZone zone = new ParkingZone();
        zone.setName(request.name());
        zone.setLevel(request.level());
        return zoneRepository.save(zone);
    }

    @Override
    public List<ParkingZone> findAllZones() {
        return zoneRepository.findAll();
    }

    @Override
    public void deleteZone(String zoneId) {
        ParkingZone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with ID: " + zoneId));
        zoneRepository.delete(zone);
    }

    @Override
    public SpotCategory createSpotCategory(SpotCategoryRequest request) {
        SpotCategory category = new SpotCategory();
        category.setType(request.type());
        return categoryRepository.save(category);
    }

    @Override
    public List<SpotCategory> findAllSpotCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public ParkingSpot createSpot(String zoneId, String categoryId) {
        ParkingSpot spot = new ParkingSpot();
        spot.setZoneId(zoneId);
        spot.setAvailable(true);
        return spotRepository.save(spot);
    }

    @Override
    public List<ParkingSpot> findAllSpots() {
        return spotRepository.findAll();
    }

    @Override
    public RevenueReportResponse generateRevenueReport(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);

        List<Payment> payments = paymentRepository.findByPaidAtBetween(start, end);
        BigDecimal totalRevenue = payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RevenueReportResponse(
                from,
                to,
                totalRevenue,
                payments.size()
        );
    }

    @Override
    @Transactional
    public void forceFreeSpot(String spotId) {
        ParkingSpot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new ResourceNotFoundException("Spot not found with ID: " + spotId));
        spot.setAvailable(true);
        spotRepository.save(spot);
    }
}