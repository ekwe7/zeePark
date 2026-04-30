package com.ekwe_hub.zeepark.repository;

import com.ekwe_hub.zeepark.model.payment.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findBySessionId(String sessionId);
    List<Payment> findByPaidAtBetween(LocalDateTime start, LocalDateTime end);
}
