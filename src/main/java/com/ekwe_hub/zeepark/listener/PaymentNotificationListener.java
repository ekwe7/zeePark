package com.ekwe_hub.zeepark.listener;

import com.ekwe_hub.zeepark.event.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentNotificationListener {

    @Async
    @EventListener
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        // In production: send email, SMS, push notification
        log.info("Payment completed: {} for session {}, amount {}",
                event.paymentId(), event.sessionId(), event.amount());
    }
}
