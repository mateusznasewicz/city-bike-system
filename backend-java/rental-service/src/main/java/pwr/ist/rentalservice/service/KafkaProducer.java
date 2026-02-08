package pwr.ist.rentalservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pwr.ist.rentalservice.entity.Wypozyczenie;
import pwr.ist.rentalservice.events.LockBikeCommand;
import pwr.ist.rentalservice.events.RentalInitEvent;
import pwr.ist.rentalservice.events.RentalPaymentCommand;
import pwr.ist.rentalservice.events.UnlockBikeCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendRentalInit(Wypozyczenie wypozyczenie){
        String rentId = wypozyczenie.getId_wypozyczenia();
        String userId = wypozyczenie.getUzytkownik().getId_uzytkownika();
        String bikeId = wypozyczenie.getRower().getId_roweru();

        RentalInitEvent event = new RentalInitEvent(userId, bikeId, rentId);
        kafkaTemplate.send("payment.events", userId, event);

        log.info("---- WYSLANO EVENT ZABLOKOWANIE SRODKOW ----");
        log.info("Event: {}", event);
    }

    public void sendProcessPayment(RentalPaymentCommand cmd){
        kafkaTemplate.send("payment.events", cmd.userId(), cmd);

        log.info("---- WYSLANO EVENT POBRANIA SRODKOW ----");
        log.info("Event: {}", cmd);
    }

    public void sendUnlockBike(UnlockBikeCommand command){
        kafkaTemplate.send("fleet.events", command.userId(), command);

        log.info("---- WYSLANO KOMENDE ODBLOKOWANIA ROWERU ----");
        log.info("Event: {}", command);
    }

    public void sendLockBike(LockBikeCommand command){
        kafkaTemplate.send("fleet.events", command.userId(), command);

        log.info("---- WYSLANO KOMENDE ZABLOKOWANIA ROWERU ----");
        log.info("Event: {}", command);
    }
}
