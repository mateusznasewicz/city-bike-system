package pwr.ist.fleetservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pwr.ist.fleetservice.events.LockBikeCommand;
import pwr.ist.fleetservice.events.UnlockBikeCommand;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "fleet.events", groupId = "fleet-service-group")
public class KafkaEventListener {

    private final FlotaService flotaService;

    @KafkaHandler
    public void handleUnlockBike(UnlockBikeCommand command){
        log.info("---- ODEBRANO EVENT ODBLOKOWANIA ROWERU ----");
        log.info("Event: {}", command);

        flotaService.unlockBike(command);
    }

    @KafkaHandler
    public void handleLockBike(LockBikeCommand command){
        log.info("---- ODEBRANO EVENT ZABLOKOWANIA ROWERU ----");
        log.info("Event: {}", command);

        flotaService.lockBike(command);
    }
}
