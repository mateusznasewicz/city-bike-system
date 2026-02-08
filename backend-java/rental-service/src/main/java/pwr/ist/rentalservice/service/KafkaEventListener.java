package pwr.ist.rentalservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pwr.ist.rentalservice.events.*;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "rental.events", groupId = "rental-service-group")
public class KafkaEventListener {

    private final KafkaProducer kafkaProducer;
    private final WypozyczenieService wypozyczenieService;

    @KafkaHandler
    public void handleFundsBlocked(FundsReservedEvent event){
        log.info("---- ODEBRANO EVENT POTWIERDZENIE ZABLOKOWANIA SRODKOW ----");
        log.info("Event: {}", event);

        UnlockBikeCommand command = new UnlockBikeCommand(event.rentalInit().userId(), event.rentalInit().bikeId(), event.rentalInit().wypozyczenieId());
        kafkaProducer.sendUnlockBike(command);
    }

    @KafkaHandler
    public void handlePaymentCompleted(PaymentCompletedEvent event){
        log.info("---- ODEBRANO EVENT POTWIERDZENIE POBRANIA SRODKOW ----");
        log.info("Event: {}", event);
        wypozyczenieService.setWypozyczenieKoszt(event.paymentCommand().wypozyczenieId(), event.koszt());

        LockBikeCommand command = new LockBikeCommand(event.paymentCommand().userId(), event.paymentCommand().bikeId(), event.paymentCommand().wypozyczenieId());
        kafkaProducer.sendLockBike(command);
    }

    @KafkaHandler
    public void handleBikeUnlocked(BikeUnlockedEvent event){
        log.info("---- ODEBRANO EVENT POTWIERDZENIE ODBLOKOWANIA ROWERU ----");
        log.info("Event: {}", event);

        wypozyczenieService.startRental(event.wypozyczenieId(), event.bikeId());
    }

    @KafkaHandler
    public void handleBikeUnlocked(BikeLockedEvent event){
        log.info("---- ODEBRANO EVENT POTWIERDZENIE ZABLOKOWANIA ROWERU ----");
        log.info("Event: {}", event);

        wypozyczenieService.endRental(event.wypozyczenieId(), event.bikeId());
    }
}
