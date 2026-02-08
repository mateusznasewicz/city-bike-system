package pwr.ist.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pwr.ist.paymentservice.events.FundsReservedEvent;
import pwr.ist.paymentservice.events.PaymentCompletedEvent;
import pwr.ist.paymentservice.events.RentalInitEvent;
import pwr.ist.paymentservice.events.RentalPaymentCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendFundsReserved(RentalInitEvent rentalInitEvent, boolean blocked){
        FundsReservedEvent event = new FundsReservedEvent(rentalInitEvent, blocked);
        kafkaTemplate.send("rental.events", rentalInitEvent.userId(), event);

        log.info("---- WYSLANO EVENT POTWIERDZENIE ZABLOKOWANIA SRODKOW ----");
        log.info("Event: {}", event);

    }

    public void sendPaymentCompleted(RentalPaymentCommand command, double koszt){
        PaymentCompletedEvent event = new PaymentCompletedEvent(command, koszt);
        kafkaTemplate.send("rental.events", command.userId(), event);

        log.info("---- WYSLANO EVENT POTWIERDZENIE POBRANIA SRODKOW ----");
        log.info("Event: {}", event);

    }
}
