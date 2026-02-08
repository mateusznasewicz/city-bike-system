package pwr.ist.fleetservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pwr.ist.fleetservice.events.BikeLockedEvent;
import pwr.ist.fleetservice.events.BikeUnlockedEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendBikeUnlocked(BikeUnlockedEvent event){
        kafkaTemplate.send("rental.events", event.userId(), event);
        log.info("---- WYSLANO EVENT ODBLOKOWANIA ROWERU ----");
        log.info("Event: {}", event);
    }

    public void sendBikeLocked(BikeLockedEvent event){
        kafkaTemplate.send("rental.events", event.userId(), event);
        log.info("---- WYSLANO EVENT ZABLOKOWANIA ROWERU ----");
        log.info("Event: {}", event);
    }
}
