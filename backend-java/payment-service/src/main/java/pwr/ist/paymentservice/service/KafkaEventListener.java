package pwr.ist.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pwr.ist.paymentservice.events.RentalInitEvent;
import pwr.ist.paymentservice.events.RentalPaymentCommand;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "payment.events", groupId = "payment-service-group")
public class KafkaEventListener {

    private final WalletService walletService;
    private final KafkaProducer kafkaProducer;

    @KafkaHandler
    public void handleRentalStarted(RentalInitEvent event){
        log.info("---- ODEBRANO EVENT PROSBE ZABLOKOWANIA SRODKOW ----");
        log.info("Event: {}", event);

        boolean blocked = walletService.blockFunds(event.userId());
        kafkaProducer.sendFundsReserved(event, blocked);
    }

    @KafkaHandler
    public void handleRentalStarted(RentalPaymentCommand event){
        log.info("---- ODEBRANO EVENT PROSBE POBRANIA SRODKOW ----");
        log.info("Event: {}", event);

        double koszt = walletService.withdrawFunds(event.userId(), event.data_rozpoczecia());
        kafkaProducer.sendPaymentCompleted(event, koszt);
    }
}
