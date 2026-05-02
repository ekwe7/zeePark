package com.ekwe_hub.zeepark.listener;

import com.ekwe_hub.zeepark.event.ParkingSessionEndedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpotReleaseListener {

    @Async
    @EventListener
    public void onSessionEnded(ParkingSessionEndedEvent event) {
        // Spot is freed synchronously in ParkingServiceImpl.endSession()
        // This listener handles any additional async post-processing
        log.info("Session {} ended, spot {} released, duration {} mins",
                event.sessionId(), event.spotId(), event.duration());
    }
}
